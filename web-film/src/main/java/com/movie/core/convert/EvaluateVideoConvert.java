package com.movie.core.convert;

import com.movie.core.constant.CoreConstant;
import com.movie.core.dto.ActorDTO;
import com.movie.core.dto.EvaluateVideoDTO;
import com.movie.core.entity.ActorEntity;
import com.movie.core.entity.EvaluateVideoEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EvaluateVideoConvert {
    @Autowired
    private UserConvert userConvert;
    @Autowired
    private VideoConvert videoConvert;

    public EvaluateVideoDTO toDTO(EvaluateVideoEntity entity) {
        EvaluateVideoDTO dto = new EvaluateVideoDTO();
        BeanUtils.copyProperties(entity, dto);
        if (entity.getUser() != null) {
            dto.setUser(userConvert.toDTO(entity.getUser()));
        }
        if (entity.getVideo() != null) {
            dto.setVideo(videoConvert.toDTO(entity.getVideo()));
        }
        return dto;
    }


    public EvaluateVideoEntity toEntity(EvaluateVideoDTO dto) {
        EvaluateVideoEntity entity = new EvaluateVideoEntity();
        BeanUtils.copyProperties(dto, entity);
        if (dto.getUser() != null) {
            entity.setUser(userConvert.toEntity(dto.getUser()));
        }
        if (dto.getVideo() != null) {
            entity.setVideo(videoConvert.toEntity(dto.getVideo()));
        }
        return entity;
    }
}
