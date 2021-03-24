package com.movie.core.service.impl;

import com.movie.core.convert.VideoConvert;
import com.movie.core.dto.VideoDTO;
import com.movie.core.entity.VideoEntity;
import com.movie.core.repository.VideoRepository;
import com.movie.core.service.IVideoService;
import com.movie.core.service.utils.PagingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VideoService implements IVideoService {

    @Autowired
    private VideoRepository videoRepository;

    @Override
    public VideoDTO save(VideoDTO videoDTO) {
        return null;
    }

    @Override
    public boolean updateVideoStatus(VideoDTO videoDTO) {
        VideoEntity videoEntity = videoRepository.getOne(videoDTO.getId());
        videoEntity.setStatus(videoDTO.getStatus());
        videoRepository.save(videoEntity);
        return true;
    }

    @Override
    public void deleteById(Long[] ids) throws Exception {
        for (Long id : ids) {
            videoRepository.deleteById(id);
        }
    }

    @Autowired
    private PagingUtils pagingUtils;

    @Autowired
    private VideoConvert videoConvert;

    @Override
    public List<VideoDTO> findByProperties(String search, String userName, Integer status, int page, int limit, String sortExpression, String sortDirection) {
        search = search.toLowerCase();
        Pageable pageable = pagingUtils.setPageable(page, limit, sortExpression, sortDirection);
        List<VideoEntity> videoEntities;
        if (userName == null || userName.isEmpty()) {
            videoEntities = videoRepository.findAllByProperties(search, pageable);
        } else {
            videoEntities = videoRepository.findAllByProperties(search, userName, pageable);
        }
        List<VideoDTO> videoDTOS = new ArrayList<>();
        for (VideoEntity entity : videoEntities) {
            if (status == null) {
                VideoDTO videoDTO = videoConvert.toDTO(entity);
                videoDTOS.add(videoDTO);
            } else if (entity.getStatus().equals(status)) {
                VideoDTO videoDTO = videoConvert.toDTO(entity);
                videoDTOS.add(videoDTO);
            }
        }
        return videoDTOS;
    }

    @Override
    public int getTotalItem(String search, String userName) {
        if (userName == null || userName.isEmpty()) {
            return (int) videoRepository.countAllByProperties(search);
        } else {
            return (int) videoRepository.countAllByProperties(search, userName);
        }
    }

    @Override
    public VideoDTO findOneById(Long id) {
        VideoEntity videoEntity = videoRepository.getOne(id);
        return videoConvert.toDTO(videoEntity);
    }
}
