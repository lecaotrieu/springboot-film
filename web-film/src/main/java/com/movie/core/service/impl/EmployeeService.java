package com.movie.core.service.impl;

import com.google.api.services.drive.model.File;
import com.movie.core.constant.CoreConstant;
import com.movie.core.convert.EmployeeConvert;
import com.movie.core.dto.EmployeeDTO;
import com.movie.core.dto.RoleDTO;
import com.movie.core.entity.EmployeeEntity;
import com.movie.core.entity.FilmEntity;
import com.movie.core.entity.RoleEntity;
import com.movie.core.repository.EmployeeRepository;
import com.movie.core.repository.FilmRepository;
import com.movie.core.service.IDriveService;
import com.movie.core.service.IEmployeeService;
import com.movie.core.service.utils.PagingUtils;
import org.apache.commons.fileupload.FileItem;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EmployeeService implements IEmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private PagingUtils pagingUtils;

    public List<EmployeeDTO> findByProperties(String search, List<String> roleCodes, int page, int limit, String sortExpression, String sortDirection) {
        search = search.toLowerCase();
        Pageable pageable = pagingUtils.setPageable(page, limit, sortExpression, sortDirection);
        List<EmployeeDTO> employeeDTOS = new ArrayList<>();
        List<EmployeeEntity> employeeEntities = employeeRepository.findAllByRolesAndUserName(roleCodes, search, pageable);
        for (EmployeeEntity entity : employeeEntities) {
            EmployeeDTO employeeDTO = executeToEmployeeDTO(entity);
            employeeDTOS.add(employeeDTO);
        }
        return employeeDTOS;
    }

    private EmployeeDTO executeToEmployeeDTO(EmployeeEntity entity) {
        List<EmployeeEntity> employees = new ArrayList<>();
        employees.add(entity);
        EmployeeDTO employeeDTO = employeeConvert.toDTO(entity);
        return employeeDTO;
    }

    public int getTotalItem(List<String> roleCodes, String search) {
        return (int) employeeRepository.countAllByRolesAndUserName(roleCodes, search);
    }

    @Override
    public int getTotalItem() {
        return (int) employeeRepository.count();
    }

    @Autowired
    private EmployeeConvert employeeConvert;

    public EmployeeDTO findOneById(Long id) {
        EmployeeEntity entity = employeeRepository.getOne(id);
        EmployeeDTO employeeDTO = executeToEmployeeDTO(entity);
        return employeeDTO;
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public EmployeeDTO save(EmployeeDTO employeeDTO) throws Exception {
        boolean check = checkEmployee(employeeDTO);
        if (!check) {
            throw new Exception();
        }
        EmployeeEntity employeeEntity;
        if (employeeDTO.getId() != null) {
            employeeEntity = employeeRepository.getOne(employeeDTO.getId());
            employeeEntity = employeeConvert.toEntity(employeeDTO, employeeEntity);
        } else {
            employeeEntity = employeeConvert.toEntity(employeeDTO);
            employeeEntity.setPassword(passwordEncoder.encode(employeeEntity.getPassword()));
        }
        List<RoleEntity> roleEntities = new ArrayList<RoleEntity>();
        for (RoleDTO roleDTO : employeeDTO.getRoles()) {
            RoleEntity roleEntity = new RoleEntity();
            roleEntity.setId(roleDTO.getId());
            roleEntities.add(roleEntity);
        }
        employeeEntity.setRoles(roleEntities);
        EmployeeDTO result = new EmployeeDTO();
        employeeEntity = employeeRepository.save(employeeEntity);
        BeanUtils.copyProperties(employeeEntity, result, "roles");
        return result;
    }

    private boolean checkEmployee(EmployeeDTO employeeDTO) {
        if (employeeDTO.getEmail().contains("@") && !employeeDTO.getUserName().contains(" ")) {
            if (employeeDTO.getUserName() == null || employeeDTO.getUserName().trim().isEmpty()) {
                return false;
            } else {
                Pattern p = Pattern.compile("[^A-Za-z0-9]");
                Matcher m = p.matcher(employeeDTO.getUserName());
                boolean check = m.find();
                if (!check) {
                    return true;
                } else {
                    return false;
                }
            }
        } else return false;

    }

    private boolean checkUpdatePassword(String newPassword) {
        if (newPassword.length() >= 6) {
            Pattern p = Pattern.compile("[^A-Za-z0-9]");
            Matcher m = p.matcher(newPassword);
            if (!m.find()) {
                return true;
            }
        }
        return false;
    }

    @Transactional
    public boolean updateEmployeeStatus(EmployeeDTO employeeDTO) {
        EmployeeEntity employeeEntity = employeeRepository.getOne(employeeDTO.getId());
        employeeEntity.setStatus(employeeDTO.getStatus());
        employeeRepository.save(employeeEntity);
        return true;
    }

    @Autowired
    private FilmRepository filmRepository;

    @Transactional
    public void deleteById(Long[] ids) {
        for (Long id : ids) {
            EmployeeEntity employeeEntity = employeeRepository.getOne(id);
            List<FilmEntity> filmEntities = employeeEntity.getFilms();
            for (FilmEntity filmEntity : filmEntities) {
                filmEntity.setEmployee(null);
                filmRepository.save(filmEntity);
            }
            if (employeeEntity.getPhoto() != null) {
                try {
                    driveService.deleteFileById(employeeEntity.getPhoto());
                } catch (IOException e) {

                }
            }
            employeeRepository.deleteById(id);
        }
    }

    @Autowired
    private IDriveService driveService;

    @Transactional
    public void updatePhoto(Long id, FileItem photo) throws IOException {
        EmployeeEntity employeeEntity = employeeRepository.getOne(id);
        if (!employeeEntity.getPhoto().isEmpty()) {
            try {
                driveService.deleteFileById(employeeEntity.getPhoto());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String photoName = "employee_avatar_" + id;
        File file = driveService.createGoogleFile(CoreConstant.AVATAR_ADDRESS_ID, photo.getContentType(), photoName, photo.getInputStream());
        driveService.createPublicPermission(file.getId());
        employeeEntity.setPhoto(file.getId());
        employeeRepository.save(employeeEntity);
    }

    @Transactional
    public void updatePassword(Long id, String password) throws Exception {
        if (!checkUpdatePassword(password)) {
            throw new Exception();
        }
        EmployeeEntity employeeEntity = employeeRepository.getOne(id);
        employeeEntity.setPassword(passwordEncoder.encode(password));
        employeeRepository.save(employeeEntity);
    }

    @Override
    public void updatePhoto(Long id, String contentType, InputStream inputStream) throws Exception {
        EmployeeEntity employeeEntity = employeeRepository.getOne(id);
        if (!employeeEntity.getPhoto().isEmpty()) {
            try {
                driveService.deleteFileById(employeeEntity.getPhoto());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String photoName = "employee_avatar_" + id;
        File file = driveService.createGoogleFile(CoreConstant.AVATAR_ADDRESS_ID, contentType, photoName, inputStream);
        driveService.createPublicPermission(file.getId());
        employeeEntity.setPhoto(file.getId());
        employeeRepository.save(employeeEntity);
    }

    @Override
    public void updatePhoto(Long id, String fileName) throws Exception {
        EmployeeEntity employeeEntity = employeeRepository.getOne(id);
        employeeEntity.setPhoto(fileName);
        employeeRepository.save(employeeEntity);
    }
}
