package com.movie.core.convert;

import com.movie.core.constant.CoreConstant;
import com.movie.core.dto.*;
import com.movie.core.entity.EmployeeEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class EmployeeConvert {
    @Autowired
    private ModelMapper modelMapper;

    public EmployeeDTO toDTO(EmployeeEntity entity) {
        EmployeeDTO dto = new EmployeeDTO();
        BeanUtils.copyProperties(entity, dto);
        if (dto.getPhoto() == null || dto.getPhoto().isEmpty()) {
            dto.setPhotoUrl("/template/image/avatar_default.jpg");
        } else {
            dto.setPhotoUrl("/" + CoreConstant.FOLDER_UPLOAD + "/" + CoreConstant.EMPLOYEE_PHOTOS + "/" + dto.getId() + "/" + dto.getPhoto());
        }
        return dto;
    }

    public EmployeeEntity toEntity(EmployeeDTO dto) {
        EmployeeEntity entity = new EmployeeEntity();
        entity = modelMapper.map(dto, EmployeeEntity.class);
        return entity;
    }

    public EmployeeEntity toEntity(EmployeeDTO dto, EmployeeEntity entity) {
//        BeanUtils.copyProperties(dto, entity, "password");
        dto.setPassword(entity.getPassword());
        entity = modelMapper.map(dto, EmployeeEntity.class);
        return entity;
    }
}
