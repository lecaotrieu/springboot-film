package com.movie.core.service.impl;

import com.google.api.services.drive.model.File;
import com.movie.core.constant.CoreConstant;
import com.movie.core.convert.UserConvert;
import com.movie.core.dto.UserDTO;
import com.movie.core.entity.UserEntity;
import com.movie.core.entity.VideoEntity;
import com.movie.core.repository.SubscribeRepository;
import com.movie.core.repository.UserRepository;
import com.movie.core.service.IDriveService;
import com.movie.core.service.ISubscribeService;
import com.movie.core.service.IUserService;
import com.movie.core.service.utils.PagingUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang3.StringUtils;
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
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserConvert userConvert;

    @Autowired
    private PagingUtils pagingUtils;

    public List<UserDTO> findByProperties(String search, int page, int limit, String sortExpression, String sortDirection) {
        search = search.toLowerCase();
        Pageable pageable = pagingUtils.setPageable(page, limit, sortExpression, sortDirection);
        List<UserDTO> userDTOS = new ArrayList<UserDTO>();
        List<UserEntity> userEntities = userRepository.findAllByUserName(search, pageable);
        for (UserEntity entity : userEntities) {
            UserDTO userDTO = userConvert.toDTO(entity);
            userDTOS.add(userDTO);
        }
        return userDTOS;
    }

    @Override
    public List<UserDTO> findByProperties(Long id) {
        List<UserDTO> userDTOS = new ArrayList<>();
        List<UserEntity> userEntities = userRepository.findAllByIds(id);
        for (UserEntity entity : userEntities) {
            UserDTO userDTO = userConvert.toDTO(entity);
            userDTOS.add(userDTO);
        }
        return userDTOS;
    }

//    @Override
//    public List<UserDTO> findByProperties(Long id) {
//        List<UserDTO> userDTOS = new ArrayList<>();
//        List<UserEntity> userEntities = userRepository.findAllById(id);
//        for (UserEntity entity : userEntities) {
//            UserDTO userDTO = userConvert.toDTO(entity);
//            userDTOS.add(userDTO);
//        }
//        return userDTOS;
//    }


    public int getTotalItem(String search) {
        return (int) userRepository.countAllByUserName(search.toLowerCase());
    }

    public UserDTO findOneById(Long id) throws Exception {
        UserEntity userEntity = userRepository.findAllById(id);
        return userConvert.toDTO(userEntity);
    }

    @Override
    public UserDTO findOneById(Long id, Integer status) throws Exception {
        UserEntity userEntity = userRepository.findAllByIdAndStatus(id, status);
        return userConvert.toDTO(userEntity);
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDTO save(UserDTO userDTO) throws Exception {
        boolean check = checkUser(userDTO);
        if (check == false) {
            throw new Exception();
        }
        UserEntity userEntity;
        if (userDTO.getId() != null) {
            userEntity = userRepository.getOne(userDTO.getId());
            userEntity = userConvert.toEntity(userDTO, userEntity);
        } else {
            userEntity = userConvert.toEntity(userDTO);
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        }
        if (userEntity.getTotalFollow() == null) {
            userEntity.setTotalFollow(0);
        }
        userEntity = userRepository.save(userEntity);
        return userConvert.toDTO(userEntity);
    }

    private boolean checkUser(UserDTO userDTO) {
        Pattern p = Pattern.compile("[^A-Za-z0-9]");
        Matcher m = p.matcher(userDTO.getUserName());
        if (userDTO.getId() == null) {
            if (!userDTO.getPassword().equals(userDTO.getConfirmPassword()) || userDTO.getPassword().length() < 6) {
                return false;
            } else {
                boolean checkPassword = p.matcher(userDTO.getPassword()).find();
                if (checkPassword) {
                    return false;
                }
            }
        }
        if (userDTO.getEmail().contains("@") && !userDTO.getUserName().contains(" ")) {
            if (userDTO.getUserName() == null || userDTO.getUserName().trim().isEmpty()) {
                return false;
            } else {
                boolean checkUserName = m.find();
                if (!checkUserName) {
                    return true;
                } else {
                    return false;
                }
            }
        } else return false;
    }

    public boolean updateUserStatus(UserDTO userDTO) {
        UserEntity userEntity = userRepository.getOne(userDTO.getId());
        userEntity.setStatus(userDTO.getStatus());
        userRepository.save(userEntity);
        return true;
    }

    @Autowired
    private IDriveService driveService;

    public void deleteById(Long[] ids) {
        for (Long id : ids) {
            //xử lý film
            //xử lý thể loại
            UserEntity userEntity = userRepository.getOne(id);
            if (userEntity.getPhoto() != null) {
                try {
                    driveService.deleteFileById(userEntity.getPhoto());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            userRepository.deleteById(id);
        }
    }

    public void updatePhotoToDrive(Long id, FileItem photo) throws IOException {
        UserEntity userEntity = userRepository.getOne(id);
        if (StringUtils.isNotBlank(userEntity.getPhoto())) {
            try {
                driveService.deleteFileById(userEntity.getPhoto());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String photoName = "user_avatar_" + id;
        File file = driveService.createGoogleFile(CoreConstant.AVATAR_ADDRESS_ID, photo.getContentType(), photoName, photo.getInputStream());
        driveService.createPublicPermission(file.getId());
        userEntity.setPhoto(file.getId());
        userRepository.save(userEntity);
    }

    @Override
    public void updatePhoto(Long id, String fileName) throws IOException {
        UserEntity userEntity = userRepository.getOne(id);
        userEntity.setPhoto(fileName);
        userRepository.save(userEntity);
    }

    public void updatePassword(Long id, String password) throws Exception {
        if (password.length() >= 6) {
            Pattern p = Pattern.compile("[^A-Za-z0-9]");
            Matcher m = p.matcher(password);
            if (m.find()) {
                throw new Exception();
            }
        }
        UserEntity userEntity = userRepository.getOne(id);
        userEntity.setPassword(passwordEncoder.encode(password));
        userRepository.save(userEntity);
    }

    @Override
    public void updatePassword(UserDTO userDTO) throws Exception {
        UserEntity userEntity = userRepository.getOne(userDTO.getId());
        boolean check = checkUpdatePassword(userEntity.getPassword(), userDTO.getPassword(), userDTO.getNewPassword(), userDTO.getConfirmPassword());
        if (check == false) {
            throw new Exception();
        }
        userEntity.setPassword(passwordEncoder.encode(userDTO.getNewPassword()));
        userRepository.save(userEntity);
    }

    @Override
    public String getPhotoId(Long userId) throws Exception {
        String userPhoto = userRepository.getOne(userId).getPhoto();
        return userPhoto;
    }

    @Override
    public void updatePhoto(Long id, String contentType, InputStream inputStream) throws Exception {
        UserEntity userEntity = userRepository.getOne(id);
        if (StringUtils.isNotBlank(userEntity.getPhoto())) {
            try {
                driveService.deleteFileById(userEntity.getPhoto());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String photoName = "user_avatar_" + id;
        File file = driveService.createGoogleFile(CoreConstant.AVATAR_ADDRESS_ID, contentType, photoName, inputStream);
        driveService.createPublicPermission(file.getId());
        userEntity.setPhoto(file.getId());
        userRepository.save(userEntity);
    }

    @Override
    public List<UserDTO> findFollower(Long id) {
        List<UserDTO> userDTOS = new ArrayList<>();
        List<UserEntity> userEntities = userRepository.findAllUserFollowMe(id, 1);
        for (UserEntity entity : userEntities) {
            UserDTO userDTO = userConvert.toDTO(entity);
            userDTOS.add(userDTO);
        }
        return userDTOS;
    }

    @Override
    public List<UserDTO> findMyFollow(Long id) {
        List<UserDTO> userDTOS = new ArrayList<>();
        List<UserEntity> userEntities = userRepository.findAllUserFollowed(id, 1);
        for (UserEntity entity : userEntities) {
            UserDTO userDTO = userConvert.toDTO(entity);
            userDTOS.add(userDTO);
        }
        return userDTOS;
    }

    @Autowired
    private SubscribeRepository subscribeRepository;

    @Transactional
    @Override
    public Integer setTotalFollow(Long id) {
        Integer totalFollow = subscribeRepository.countAllByFollowIsLikeAndUserFollow_Id(1, id);
        UserEntity userEntity = userRepository.getOne(id);
        userEntity.setTotalFollow(totalFollow);
        userRepository.save(userEntity);
        return totalFollow;
    }

    private boolean checkUpdatePassword(String userEntityPassword, String password, String newPassword, String confirmPassword) {
        if (passwordEncoder.matches(password, userEntityPassword)) {
            if (newPassword.length() >= 6 && newPassword.equals(confirmPassword)) {
                Pattern p = Pattern.compile("[^A-Za-z0-9]");
                Matcher m = p.matcher(newPassword);
                if (!m.find()) {
                    return true;
                }
            }
        }
        return false;
    }
}
