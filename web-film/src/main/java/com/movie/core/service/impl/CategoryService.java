package com.movie.core.service.impl;

import com.movie.core.dto.CategoryDTO;
import com.movie.core.entity.CategoryEntity;
import com.movie.core.repository.CategoryRepository;
import com.movie.core.service.ICategoryService;
import com.movie.core.service.IFilmService;
import com.movie.core.service.utils.StringGlobalUtils;
import com.movie.core.convert.CategoryConvert;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService implements ICategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private StringGlobalUtils stringGlobalUtils;

    @Autowired
    private IFilmService filmService;
    public List<CategoryDTO> findAll() {
        List<CategoryDTO> results = new ArrayList<CategoryDTO>();
        for (CategoryEntity categoryEntity : categoryRepository.findAll()) {
            CategoryDTO categoryDTO = categoryConvert.toDTO(categoryEntity);
            categoryDTO.setTotalFilm(filmService.getTotalItemByCategory(categoryDTO.getCode(),1));
            results.add(categoryDTO);
        }
        return results;
    }

    @Autowired
    private CategoryConvert categoryConvert;

    public List<CategoryDTO> findByProperties(String search, int page, int limit, String sortExpression, String sortDirection) {
        Sort sort = null;
        search = search.toLowerCase();
        if (sortExpression != null && sortDirection != null) {
            sort = Sort.by(sortDirection.equals("1") ? Sort.Direction.ASC : Sort.Direction.DESC, sortExpression);
        }
        Pageable pageable = PageRequest.of(page - 1, limit, sort);
        List<CategoryDTO> categoryDTOS = new ArrayList<CategoryDTO>();
        List<CategoryEntity> categoryEntities = categoryRepository.findAllByNameOrCode(search, search, pageable);
        for (CategoryEntity entity : categoryEntities) {
            CategoryDTO categoryDTO = categoryConvert.toDTO(entity);
            categoryDTOS.add(categoryDTO);
        }
        return categoryDTOS;
    }

    public int getTotalItem(String search) {
        return (int) categoryRepository.countAllByNameOrCode(search, search);
    }

    public CategoryDTO findOneById(Long id) {
        return categoryConvert.toDTO(categoryRepository.getOne(id));
    }

    @Transactional
    public CategoryDTO save(CategoryDTO categoryDTO) throws IOException {
        CategoryEntity entity;
        if (categoryDTO.getId()!=null){
            entity = categoryRepository.getOne(categoryDTO.getId());
            entity.setCode(categoryDTO.getCode());
            entity.setName(categoryDTO.getName());
            entity.setDescription(categoryDTO.getDescription());
        } else{
            entity = categoryConvert.toEntity(categoryDTO);
        }
        if (entity.getCode() == null) {
            entity.setCode(stringGlobalUtils.covertToString(entity.getName()));
        }
        entity = categoryRepository.save(entity);
        return categoryConvert.toDTO(entity);
    }

    @Transactional
    public void delete(Long[] ids) {
        for (Long id : ids) {
            CategoryEntity categoryEntity = categoryRepository.getOne(id);
            categoryEntity.setFilms(null);
            categoryRepository.save(categoryEntity);
            categoryRepository.deleteById(id);
        }
    }
}
