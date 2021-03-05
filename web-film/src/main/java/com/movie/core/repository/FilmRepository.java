package com.movie.core.repository;

import com.movie.core.entity.FilmEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FilmRepository extends JpaRepository<FilmEntity, Long> {
    @Query("select distinct f from FilmEntity f join f.categories c where f.employee.userName=?1 and (lower(f.name) like %?2% or lower(f.code) like %?2%) and f.status=?3 and f.filmType.code like %?4% and f.country.code like %?6% and (f.year like %?7%) and c.code like %?5% ")
    List<FilmEntity> findAllByPosterAndProperties(String userName, String search, Integer status, String filmType, String category, String country, String year, Pageable pageable);

    @Query("select distinct f from FilmEntity f join f.categories c where (lower(f.name) like %?1% or lower(f.code) like %?2%) and f.filmType.code like %?3% and f.country.code like %?5% and f.year like %?6% and c.code like %?4%")
    List<FilmEntity> findAllByAdminAndProperties(String name, String code, String filmType, String category, String country, String year, Pageable pageable);

    @Query("select count(distinct f.id) from FilmEntity f join f.categories c where f.employee.userName=?1 and (lower(f.name) like %?2% or lower(f.code) like %?2%) and f.status=?3 and f.filmType.code like %?4% and f.country.code like %?6% and f.year like %?7% and c.code like %?5%")
    long countAllByPosterAndProperties(String userName, String search, Integer status, String filmType, String category, String country, String year);

    @Query("select count(distinct f.id) from FilmEntity f join f.categories c where (lower(f.name) like %?1% or lower(f.code) like %?2%)  and f.filmType.code like %?3% and f.country.code like %?5% and f.year like %?6% and c.code like %?4%")
    long countAllByAdminAndProperties(String name, String code, String filmType, String category, String country, String year);

    List<FilmEntity> findAllByStatus(Integer status, Pageable pageable);

    List<FilmEntity> findAllByStatusAndFilmType_Code(Integer status, String filmTypeCode, Pageable pageable);

    @Query("select f from FilmEntity f where f.status=?1 and f.episodes.size = 0 and f.trailer <> 'null'")
    List<FilmEntity> findAllByStatusAndTrailerNotNull(Integer status, Pageable pageable);

    @Query("select distinct f from FilmEntity f  join f.evaluates e  where f.status=?2 and e.user.id = ?1 and e.follow=1")
    List<FilmEntity> findAllByUserFollow(Long userId, Integer status, Pageable pageable);

    long countAllByIdAndEmployee_UserNameAndStatus(Long id, String username, Integer status);

    FilmEntity findByCode(String code);

    FilmEntity findByIdAndStatus(Long id, Integer status);

    FilmEntity findByIdAndCreatedByAndStatus(Long id, String poster, Integer status);

    FilmEntity findByIdAndCodeAndStatus(Long id, String code, Integer status);

    @Query("select distinct f from FilmEntity f join f.categories c where (lower(f.name) like %?1% or lower(f.code) like %?1%) and f.filmType.code like %?2% and f.country.code like %?4% and f.year like %?5% and c.code like %?3% and f.status = ?6")
    List<FilmEntity> findAllByProperties(String search, String filmType, String category, String country, String year, Integer status, Pageable pageable);

    @Query("select count(distinct f.id) from FilmEntity f join f.categories c where (lower(f.name) like %?1% or lower(f.code) like %?1%) and f.filmType.code like %?2% and f.country.code like %?4% and f.year like %?5% and c.code like %?3% and f.status = ?6")
    long countAllByProperties(String search, String filmType, String category, String country, String year, Integer status);
}
