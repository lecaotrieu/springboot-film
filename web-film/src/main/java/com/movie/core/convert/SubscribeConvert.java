package com.movie.core.convert;

import com.movie.core.dto.SubscribeDTO;
import com.movie.core.entity.SubscribeEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SubscribeConvert {
    @Autowired
    private UserConvert userConvert;

    public SubscribeDTO toDTO(SubscribeEntity entity) {
        SubscribeDTO dto = new SubscribeDTO();
        BeanUtils.copyProperties(entity, dto);
        if (entity.getUser() != null) {
            dto.setUser(userConvert.toDTO(entity.getUser()));
        }
        if (entity.getUserFollow() != null) {
            dto.setUserFollow(userConvert.toDTO(entity.getUserFollow()));
        }
        return dto;
    }


    public SubscribeEntity toEntity(SubscribeDTO dto) {
        SubscribeEntity entity = new SubscribeEntity();
        BeanUtils.copyProperties(dto, entity);
        if (dto.getUser() != null) {
            entity.setUser(userConvert.toEntity(dto.getUser()));
        }
        if (dto.getUserFollow() != null) {
            entity.setUserFollow(userConvert.toEntity(dto.getUserFollow()))     ;
        }
        return entity;
    }
}
