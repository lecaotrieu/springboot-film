package com.movie.core.service.impl;

import com.movie.core.constant.CoreConstant;
import com.movie.core.convert.EvaluateConvert;
import com.movie.core.dto.EvaluateDTO;
import com.movie.core.entity.EvaluateEntity;
import com.movie.core.entity.FilmEntity;
import com.movie.core.entity.UserEntity;
import com.movie.core.repository.EpisodeRepository;
import com.movie.core.repository.EvaluateRepository;
import com.movie.core.service.IEvaluateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class EvaluateService implements IEvaluateService {
    @Autowired
    private EvaluateRepository evaluateRepository;
    @Autowired
    private EvaluateConvert evaluateConvert;
    @Autowired
    private EpisodeRepository episodeRepository;

    public List<EvaluateDTO> findAllByUserId(Long id, Integer status) throws Exception {
        List<EvaluateDTO> evaluateDTOS = new ArrayList<EvaluateDTO>();
        List<EvaluateEntity> evaluateEntities = evaluateRepository.findAllByUser_IdAndStatus(id, status);
        for (EvaluateEntity evaluateEntity : evaluateEntities) {
            EvaluateDTO evaluateDTO = evaluateConvert.toDTO(evaluateEntity);
            evaluateDTO.getFilm().setEpisodesCount(episodeRepository.countAllByFilm_IdAndStatus(evaluateDTO.getFilm().getId(), CoreConstant.ACTIVE_STATUS));
            evaluateDTOS.add(evaluateDTO);
        }
        return evaluateDTOS;
    }

    public EvaluateDTO findOneByUserAndFilm(Long filmId, Long userId) {
        EvaluateEntity evaluateEntity = evaluateRepository.findByFilm_IdAndUser_IdAndStatus(filmId, userId, CoreConstant.ACTIVE_STATUS);
        if (evaluateEntity != null) {
            return evaluateConvert.toDTO(evaluateEntity);
        }
        return new EvaluateDTO();
    }

    @Transactional
    public Long save(EvaluateDTO evaluateDTO) {
        EvaluateEntity entity;
        if (evaluateDTO.getId() != null) {
            entity = evaluateRepository.getOne(evaluateDTO.getId());
            entity = evaluateConvert.toEntity(entity, evaluateDTO);
        } else {
            entity = evaluateConvert.toEntity(evaluateDTO);
            entity.setWatched(0);
            entity.setFollow(0);
        }
        entity = evaluateRepository.save(entity);
        return entity.getId();
    }

    public Long updateFollow(EvaluateDTO evaluateDTO) {
        EvaluateEntity entity;
        entity = evaluateRepository.findByFilm_IdAndUser_IdAndStatus(evaluateDTO.getFilm().getId(), evaluateDTO.getUser().getId(), CoreConstant.ACTIVE_STATUS);
        entity.setFollow(evaluateDTO.getFollow());
        entity = evaluateRepository.save(entity);
        return entity.getId();
    }

    @Override
    public void delete(Long[] ids) throws Exception {
        for (Long id : ids) {
            episodeRepository.deleteById(id);
        }
    }

    @Override
    public Integer updateLike(Long filmId, Integer like, Long userId) {
        EvaluateEntity entity = evaluateRepository.findByFilm_IdAndUser_Id(filmId, userId);
        if (entity != null) {
            entity.setLiked(like);
        } else {
            FilmEntity filmEntity = new FilmEntity();
            filmEntity.setId(filmId);
            UserEntity userEntity = new UserEntity();
            userEntity.setId(userId);
            entity = new EvaluateEntity();
            entity.setFilm(filmEntity);
            entity.setUser(userEntity);
            entity.setLiked(like);
        }
        entity =  evaluateRepository.save(entity);
        return entity.getLiked();
    }

    @Override
    public Integer updateFollow(Long filmId, Integer follow, Long userId) {
        EvaluateEntity entity = evaluateRepository.findByFilm_IdAndUser_Id(filmId, userId);
        if (entity != null) {
            entity.setFollow(follow);
        } else {
            FilmEntity filmEntity = new FilmEntity();
            filmEntity.setId(filmId);
            UserEntity userEntity = new UserEntity();
            userEntity.setId(userId);
            entity = new EvaluateEntity();
            entity.setFilm(filmEntity);
            entity.setUser(userEntity);
            entity.setFollow(follow);
        }
        entity =  evaluateRepository.save(entity);
        return entity.getFollow();
    }

    @Override
    public Integer updateScores(Long filmId, Integer scores, Long userId) {
        EvaluateEntity entity = evaluateRepository.findByFilm_IdAndUser_Id(filmId, userId);
        if (entity != null) {
            entity.setScores(scores);
        } else {
            FilmEntity filmEntity = new FilmEntity();
            filmEntity.setId(filmId);
            UserEntity userEntity = new UserEntity();
            userEntity.setId(userId);
            entity = new EvaluateEntity();
            entity.setFilm(filmEntity);
            entity.setUser(userEntity);
            entity.setScores(scores);
        }
        entity =  evaluateRepository.save(entity);
        return entity.getScores();
    }

    @Override
    public Double getAvgScores(Long filmId) {
        return evaluateRepository.avgRating(filmId);
    }

    @Override
    public Integer getTotalVote(Long filmId) {
        return evaluateRepository.countAllByFilm_IdAndStatus(filmId, 1);
    }

}
