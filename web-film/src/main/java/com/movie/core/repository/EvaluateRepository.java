package com.movie.core.repository;

import com.movie.core.entity.EvaluateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EvaluateRepository extends JpaRepository<EvaluateEntity, Long> {
    List<EvaluateEntity> findAllByFilm_IdAndStatus(Long filmId, Integer status);

    List<EvaluateEntity> findAllByUser_IdAndStatus(Long userId, Integer status);

    EvaluateEntity findByFilm_IdAndUser_IdAndStatus(Long filmId, Long userId, Integer status);

    EvaluateEntity findByFilm_IdAndUser_Id(Long filmId, Long userId);

    @Query("select avg(e.scores) from EvaluateEntity e where e.film.id = ?1 and e.status = 1")
    Double avgRating(Long filmId);

    Integer countAllByFilm_IdAndStatus(Long filmId, Integer status);
}
