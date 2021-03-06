package com.movie.core.service;

import com.movie.core.dto.EmployeeDTO;
import org.apache.commons.fileupload.FileItem;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface IEmployeeService {
    List<EmployeeDTO> findByProperties(String search, List<String> roleCodes, int page, int limit, String sortExpression, String sortDirection);

    int getTotalItem(List<String> roleCodes, String search);

    int getTotalItem();

    EmployeeDTO findOneById(Long id);

    EmployeeDTO save(EmployeeDTO employeeDTO) throws Exception;

    boolean updateEmployeeStatus(EmployeeDTO employeeDTO);

    void deleteById(Long[] ids);

    void updatePhoto(Long id, FileItem photo) throws IOException;

    void updatePassword(Long id, String password) throws Exception;

    void updatePhoto(Long id, String contentType, InputStream inputStream) throws Exception;

    void updatePhoto(Long id, String fileName) throws Exception;

}
