package com.movie.core.service;

import com.movie.core.dto.EvaluateDTO;

import java.util.List;

public interface IEvaluateService {
    List<EvaluateDTO> findAllByUserId(Long id, Integer status) throws Exception;

    EvaluateDTO findOneByUserAndFilm(Long filmId, Long userId);

    Long save(EvaluateDTO evaluateDTO);

    Long updateFollow(EvaluateDTO evaluateDTO);

    void delete(Long[] ids) throws Exception;

    Integer updateLike(Long filmId, Integer like, Long userId);

    Integer updateFollow(Long filmId, Integer follow, Long userId);

    Integer updateScores(Long filmId, Integer scores, Long userId);

    Double getAvgScores(Long filmId);

    Integer getTotalVote(Long filmId);
}
