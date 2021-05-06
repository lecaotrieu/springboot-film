package com.movie.core.service;

import com.movie.core.dto.CategoryDTO;
import com.movie.core.dto.FilmDTO;
import com.movie.core.dto.VideoDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IVideoService {
    VideoDTO save(VideoDTO videoDTO) throws Exception;

    boolean updateVideoStatus(VideoDTO videoDTO);

    void deleteById(Long[] ids) throws Exception;

    List<VideoDTO> findByProperties(String search, String userName, Integer status, int page, int limit, String sortExpression, String sortDirection);

    List<VideoDTO> findAll();

    String uploadVideoToDrive(Long videoId, MultipartFile video) throws IOException;

    int getTotalItem(String search, String userName);

    VideoDTO findOneById(Long id);

    VideoDTO findOneById(Long id, Long userId);

    boolean checkUser(Long userId, Long videoId);

    List<VideoDTO> findByProperties(String search, Integer status, int page, int limit, String sortExpression, String sortDirection);

    int getTotalItem(String search, Integer status);
}
