package com.movie.core.convert;

import com.movie.core.constant.CoreConstant;
import com.movie.core.dto.ActorDTO;
import com.movie.core.entity.ActorEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ActorConvert {
    public ActorDTO toDTO(ActorEntity entity) {
        ActorDTO dto = new ActorDTO();
        BeanUtils.copyProperties(entity, dto, "films");
        if (entity.getFilms() != null)
            dto.setTotalFilm(entity.getFilms().size());
        if (dto.getAvatar() == null || dto.getAvatar().isEmpty()) {
            dto.setAvatarUrl("/template/image/avatar_default.jpg");
        } else {
            dto.setAvatarUrl("/" + CoreConstant.FOLDER_UPLOAD + "/" + CoreConstant.ACTOR_PHOTOS + "/" + dto.getId() + "/" + dto.getAvatar());
        }
        return dto;
    }


    public ActorEntity toEntity(ActorDTO dto) {
        ActorEntity entity = new ActorEntity();
        BeanUtils.copyProperties(dto, entity, "films");
        return entity;
    }
}
