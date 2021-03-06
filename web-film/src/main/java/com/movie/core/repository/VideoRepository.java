package com.movie.core.repository;

import com.movie.core.entity.VideoEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VideoRepository extends JpaRepository<VideoEntity, Long> {
    @Query("select v from VideoEntity v where (lower(v.code) like %?1% or lower(v.name) like %?1%)")
    List<VideoEntity> findAllByProperties(String search, Pageable pageable);

    @Query("select v from VideoEntity v where (lower(v.code) like %?1% or lower(v.name) like %?1%) and v.status = ?2")
    List<VideoEntity> findAllByProperties(String search, Integer status, Pageable pageable);

    @Query("select v from VideoEntity v where (lower(v.code) like %?1% or lower(v.name) like %?1%) and v.user.userName like ?2%")
    List<VideoEntity> findAllByProperties(String search, String userName, Pageable pageable);

    @Query("select v from VideoEntity v where (lower(v.code) like %?1% or lower(v.name) like %?1%) and v.user.userName like ?2% and v.status = ?3")
    List<VideoEntity> findAllByProperties(String search, String userName, Integer status, Pageable pageable);

    @Query("select count(v.id) from VideoEntity v where (lower(v.code) like %?1% or lower(v.name) like %?1%)")
    long countAllByProperties(String search);

    @Query("select count(v.id) from VideoEntity v where (lower(v.code) like %?1% or lower(v.name) like %?1%) and v.status = ?2")
    long countAllByProperties(String search, Integer status);

    @Query("select count(v.id) from VideoEntity v where (lower(v.code) like %?1% or lower(v.name) like %?1%) and v.user.userName like ?2%")
    long countAllByProperties(String search, String userName);

    @Query("select count(v.id) from VideoEntity v where (lower(v.code) like %?1% or lower(v.name) like %?1%) and v.user.userName like ?2% and v.status = ?3")
    long countAllByProperties(String search, String userName, Integer status);

    VideoEntity findByIdAndUser_Id(Long id, Long userId);

    VideoEntity findByIdAndStatus(Long id, Integer status);

    List<VideoEntity> findAllByStatus(Integer status);

    List<VideoEntity> findAllByUser_IdAndStatus(Long userId, Integer status);

    List<VideoEntity> findAllByUser_Id(Long userId);

    @Query("select v from VideoEntity v where (lower(v.code) like %?1% or lower(v.name) like %?1%) and v.status = ?2")
    List<VideoEntity> findAllBySearchAndStatus(String search, Integer status, Pageable pageable);

    @Query("select count(v.id) from VideoEntity v where (lower(v.code) like %?1% or lower(v.name) like %?1%) and v.status=?2")
    long countAllBySearchAndStatus(String search, Integer status);
}
