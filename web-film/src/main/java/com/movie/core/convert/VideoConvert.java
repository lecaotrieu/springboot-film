package com.movie.core.convert;

import com.movie.core.dto.VideoDTO;
import com.movie.core.entity.VideoEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class VideoConvert {
    public VideoDTO toDTO(VideoEntity entity) {
        VideoDTO dto = new VideoDTO();
        BeanUtils.copyProperties(entity, dto);
        if (dto.getVideo() != null && !dto.getVideo().isEmpty()) {
            dto.setVideoUrl("https://drive.google.com/uc?id=" + dto.getVideo());
        }
        if (entity.getCommentVideos()!=null){
            dto.setTotalComment(entity.getCommentVideos().size());
        }
        return dto;
    }

    public VideoEntity toEntity(VideoDTO dto) {
        VideoEntity entity = new VideoEntity();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
}
