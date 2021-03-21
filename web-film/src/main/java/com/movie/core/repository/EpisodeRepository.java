package com.movie.core.repository;

import com.movie.core.entity.EpisodeEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EpisodeRepository extends JpaRepository<EpisodeEntity, Long> {
    int countAllByFilm_Id(Long id);

    int countAllByFilm_IdAndStatus(Long id, Integer status);

    List<EpisodeEntity> findByFilm_IdAndStatus(Long id, Integer status, Pageable pageable);

    List<EpisodeEntity> findByFilm_IdAndStatusAndNameContaining(Long id, Integer status, String name, Pageable pageable);

    @Query("select distinct e from EpisodeEntity e where e.film.id = ?1 and (lower(e.name) like %?2% or lower(e.film.name) like %?2% or lower(e.film.code) like %?2%) and e.status = ?3")
    List<EpisodeEntity> findByProperties(Long filmId, String search, Integer status, Pageable pageable);

    @Query("select distinct e from EpisodeEntity e where (lower(e.name) like %?1% or lower(e.film.name) like %?1% or lower(e.film.code) like %?1%) and e.status = ?2")
    List<EpisodeEntity> findByProperties(String search, Integer status, Pageable pageable);

    @Query("select distinct e from EpisodeEntity e where (lower(e.name) like %?2% or lower(e.film.name) like %?2% or lower(e.film.code) like %?2%) and e.film.id = ?1")
    List<EpisodeEntity> findByProperties(Long filmId, String search, Pageable pageable);

    @Query("select distinct e from EpisodeEntity e where (lower(e.name) like %?1% or lower(e.film.name) like %?1% or lower(e.film.code) like %?1%)")
    List<EpisodeEntity> findByProperties(String search, Pageable pageable);

    EpisodeEntity findByFilm_CodeAndEpisodeCode(String filmCode, Integer code);

    EpisodeEntity findByIdAndStatus(Long id, Integer status);

    List<EpisodeEntity> findAllByStatusAndFilm_Id(Integer status, Long filmId);

    List<EpisodeEntity> findAllByFilm_Id(Long filmId);

    Integer countByFilm_Employee_UserNameAndId(String username, Long id);

    List<EpisodeEntity> findByFilm_IdAndNameContaining(Long id, String search, Pageable pageable);

    @Query("select count(distinct e.id) from EpisodeEntity e where e.film.id = ?1 and (lower(e.name) like %?2% or lower(e.film.name) like %?2% or lower(e.film.code) like %?2%) and e.status = ?3")
    int countByProperties(Long id, String search, int status);

    @Query("select count(distinct e.id) from EpisodeEntity e where (lower(e.name) like %?1% or lower(e.film.name) like %?1% or lower(e.film.code) like %?1%) and e.status = ?2")
    int countByProperties(String search, int status);

    @Query("select count(distinct e.id) from EpisodeEntity e where (lower(e.name) like %?2% or lower(e.film.name) like %?2% or lower(e.film.code) like %?2%) and e.film.id = ?1")
    int countByProperties(Long id, String search);

    @Query("select count(distinct e.id) from EpisodeEntity e where (lower(e.name) like %?1% or lower(e.film.name) like %?1% or lower(e.film.code) like %?1%)")
    int countByProperties(String search);
}
