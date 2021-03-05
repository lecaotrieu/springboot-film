package com.movie.core.convert;

import com.movie.core.dto.CategoryDTO;
import com.movie.core.entity.CategoryEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class CategoryConvert {
    public CategoryDTO toDTO(CategoryEntity entity) {
        CategoryDTO dto = new CategoryDTO();
        BeanUtils.copyProperties(entity, dto, "films");
        if (entity.getFilms() != null)
            dto.setTotalFilm(entity.getFilms().size());
        return dto;
    }


    public CategoryEntity toEntity(CategoryDTO dto) {
        CategoryEntity entity = new CategoryEntity();
        BeanUtils.copyProperties(dto, entity, "films");
        return entity;
    }
}
