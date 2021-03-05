package com.movie.core.convert;

import com.movie.core.constant.CoreConstant;
import com.movie.core.dto.UserDTO;
import com.movie.core.entity.EmployeeEntity;
import com.movie.core.entity.UserEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserConvert {
    public UserDTO toDTO(UserEntity entity) {
        UserDTO dto = new UserDTO();
        BeanUtils.copyProperties(entity, dto);
        if (dto.getPhoto() == null || dto.getPhoto().isEmpty()) {
            dto.setPhotoUrl("/template/image/avatar_default.jpg");
        } else {
            dto.setPhotoUrl("/" + CoreConstant.FOLDER_UPLOAD + "/" + CoreConstant.USER_PHOTOS + "/" + dto.getId() + "/" + dto.getPhoto());
        }
        return dto;
    }

    @Autowired
    private ModelMapper modelMapper;

    public UserEntity toEntity(UserDTO dto) {
        UserEntity entity = new UserEntity();
        entity = modelMapper.map(dto, UserEntity.class);
        return entity;
    }

    public UserEntity toEntity(UserDTO userDTO, UserEntity userEntity) {
        BeanUtils.copyProperties(userDTO, userEntity, "photo", "password");
        return userEntity;
    }
}
