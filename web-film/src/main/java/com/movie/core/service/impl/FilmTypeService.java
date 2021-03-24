package com.movie.core.service.impl;

import com.movie.core.dto.CategoryDTO;
import com.movie.core.dto.FilmTypeDTO;
import com.movie.core.entity.CategoryEntity;
import com.movie.core.entity.FilmTypeEntity;
import com.movie.core.repository.FilmTypeRepository;
import com.movie.core.service.IFilmTypeService;
import com.movie.core.convert.FilmTypeConvert;
import com.movie.core.service.utils.StringGlobalUtils;
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
public class FilmTypeService implements IFilmTypeService {
    @Autowired
    private FilmTypeRepository filmTypeRepository;

    @Autowired
    private FilmTypeConvert filmTypeConvert;

    public List<FilmTypeDTO> findAll() {
        List<FilmTypeDTO> results = new ArrayList<FilmTypeDTO>();
        for (FilmTypeEntity filmTypeEntity : filmTypeRepository.findAll()) {
            FilmTypeDTO filmTypeDTO = filmTypeConvert.toDTO(filmTypeEntity);
            results.add(filmTypeDTO);
        }
        return results;
    }

    public FilmTypeDTO findOneById(Long id) {
        return filmTypeConvert.toDTO(filmTypeRepository.getOne(id));
    }

    public List<FilmTypeDTO> findByProperties(String search, int page, int limit, String sortExpression, String sortDirection) {
        Sort sort = null;
        search = search.toLowerCase();
        if (sortExpression != null && sortDirection != null) {
            sort = Sort.by(sortDirection.equals("1") ? Sort.Direction.ASC : Sort.Direction.DESC, sortExpression);
        }
        Pageable pageable = PageRequest.of(page - 1, limit, sort);
        List<FilmTypeDTO> filmTypeDTOS = new ArrayList<FilmTypeDTO>();
        List<FilmTypeEntity> filmTypeEntities = filmTypeRepository.findAllByNameOrCode(search, search, pageable);
        for (FilmTypeEntity entity : filmTypeEntities) {
            FilmTypeDTO filmTypeDTO = filmTypeConvert.toDTO(entity);
            filmTypeDTOS.add(filmTypeDTO);
        }
        return filmTypeDTOS;
    }

    public int getTotalItem(String search) {
        return (int) filmTypeRepository.countAllByNameOrCode(search, search);
    }

    @Autowired
    private StringGlobalUtils stringGlobalUtils;

    @Transactional
    public FilmTypeDTO save(FilmTypeDTO filmTypeDTO) throws IOException {
        FilmTypeEntity entity;
        if (filmTypeDTO.getId() != null) {
            entity = filmTypeRepository.getOne(filmTypeDTO.getId());
            entity.setCode(filmTypeDTO.getCode());
            entity.setName(filmTypeDTO.getName());
        } else {
            entity = filmTypeConvert.toEntity(filmTypeDTO);
        }
        if (entity.getCode() == null) {
            entity.setCode(stringGlobalUtils.covertToString(entity.getName()));
        }
        entity = filmTypeRepository.save(entity);
        return filmTypeConvert.toDTO(entity);
    }

    public void delete(Long[] ids) {
        for (Long id : ids) {
            FilmTypeEntity filmTypeEntity = filmTypeRepository.getOne(id);
            filmTypeEntity.setFilms(null);
            filmTypeRepository.save(filmTypeEntity);
            filmTypeRepository.deleteById(id);
        }
    }
}
