package com.movie.core.service.impl;

import com.movie.core.convert.ActorConvert;
import com.movie.core.dto.ActorDTO;
import com.movie.core.entity.ActorEntity;
import com.movie.core.repository.ActorRepository;
import com.movie.core.service.IActorService;
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
public class ActorService implements IActorService {
    @Autowired
    private ActorRepository actorRepository;

    public List<ActorDTO> findAll() {
        List<ActorDTO> results = new ArrayList<ActorDTO>();
        for (ActorEntity actorEntity : actorRepository.findAll()) {
            ActorDTO actorDTO = actorConvert.toDTO(actorEntity);
            results.add(actorDTO);
        }
        return results;
    }

    @Autowired
    private ActorConvert actorConvert;

    @Autowired
    private StringGlobalUtils stringGlobalUtils;

    @Transactional
    public ActorDTO save(ActorDTO actorDTO) throws IOException {
        ActorEntity entity;
        if (actorDTO.getId()!=null){
            entity = actorRepository.getOne(actorDTO.getId());
            entity.setCode(actorDTO.getCode());
            entity.setName(actorDTO.getName());
            entity.setDescription(actorDTO.getDescription());
        } else{
            entity = actorConvert.toEntity(actorDTO);
        }
        if (entity.getCode() == null) {
            entity.setCode(stringGlobalUtils.covertToString(entity.getName()));
        }
        entity = actorRepository.save(entity);
        return actorConvert.toDTO(entity);
    }

    public ActorDTO findOneById(Long id) {
        return actorConvert.toDTO(actorRepository.getOne(id));
    }

    public List<ActorDTO> findByProperties(String search, int page, int limit, String sortExpression, String sortDirection) {
        Sort sort = null;
        search = search.toLowerCase();
        if (sortExpression != null && sortDirection != null) {
            sort = Sort.by(sortDirection.equals("1") ? Sort.Direction.ASC : Sort.Direction.DESC, sortExpression);
        }
        Pageable pageable = PageRequest.of(page - 1, limit, sort);
        List<ActorDTO> actorDTOS = new ArrayList<ActorDTO>();
        List<ActorEntity> actorEntities = actorRepository.findAllByNameOrCode(search, search, pageable);
        for (ActorEntity entity : actorEntities) {
            ActorDTO actorDTO = actorConvert.toDTO(entity);
            actorDTOS.add(actorDTO);
        }
        return actorDTOS;
    }

    public int getTotalItem(String search) {
        return (int) actorRepository.countAllByNameOrCode(search, search);
    }

    public void delete(Long[] ids) {
        for (Long id : ids) {
            ActorEntity actorEntity = actorRepository.getOne(id);
            actorEntity.setFilms(null);
            actorRepository.save(actorEntity);
            actorRepository.deleteById(id);
        }
    }
}
