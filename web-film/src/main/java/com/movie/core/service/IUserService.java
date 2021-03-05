package com.movie.core.service;

import com.movie.core.dto.UserDTO;
import org.apache.commons.fileupload.FileItem;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface IUserService {
    List<UserDTO> findByProperties(String search, int page, int limit, String sortExpression, String sortDirection);

    int getTotalItem(String search);

    UserDTO findOneById(Long id) throws Exception;

    UserDTO save(UserDTO userDTO) throws Exception;

    boolean updateUserStatus(UserDTO userDTO);

    void deleteById(Long[] ids);

    void updatePhotoToDrive(Long id, FileItem photo) throws IOException;

    void updatePhoto(Long id, String fileName) throws IOException;

    void updatePassword(Long id, String password) throws Exception;

    void updatePassword(UserDTO userDTO) throws Exception;

    String getPhotoId(Long userId) throws Exception;

    void updatePhoto(Long id, String contentType, InputStream inputStream) throws Exception;
}
