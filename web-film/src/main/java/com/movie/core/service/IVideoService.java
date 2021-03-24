package com.movie.core.service;

import com.movie.core.dto.VideoDTO;

import java.util.List;

public interface IVideoService {
    VideoDTO save(VideoDTO videoDTO);

    boolean updateVideoStatus(VideoDTO videoDTO);

    void deleteById(Long[] ids) throws Exception;

    List<VideoDTO> findByProperties(String search,String userName, Integer status, int page, int limit, String sortExpression, String sortDirection);

    int getTotalItem(String search, String userName);

    VideoDTO findOneById(Long id);
}
