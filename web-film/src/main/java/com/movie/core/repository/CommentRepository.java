package com.movie.core.repository;

import com.movie.core.entity.CommentEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findAllByFilm_IdAndCommentIsNull(Long filmId, Pageable pageable);

    List<CommentEntity> findAllByComment_IdAndFilm_Id(Long commentId, Long filmId, Pageable pageable);

    long countAllByFilm_Id(Long id);

    long countAllByFilm_IdAndComment_Id(Long commentId, Long filmId);

    @Query("select c from CommentEntity c where c.comment.id = ?1 and c.user.userName like %?2% and lower(c.content) like %?3%")
    List<CommentEntity> findAllByCommentIdAndUser(Long commentId, String userName, String search, Pageable pageable);

    @Query("select c from CommentEntity c where c.film.id = ?1 and c.user.userName like %?2% and lower(c.content) like %?3% and c.comment is null")
    List<CommentEntity> findAllByFilmIdAndUser(Long filmId, String userName, String search, Pageable pageable);

    @Query("select c from CommentEntity c where c.user.userName like %?1% and (lower(c.content) like %?2% or lower(c.film.name) like %?2% or lower(c.film.code) like %?2%) and c.comment is null")
    List<CommentEntity> findAllByUserAndProperties(String userName, String search, Pageable pageable);

    @Query("select count (c.id) from CommentEntity c where c.comment.id = ?1 and c.user.userName like %?2% and lower(c.content) like %?3%")
    long countAllByCommentIdAndUser(Long commentId, String userName, String search);

    @Query("select count (c.id)from CommentEntity c where c.film.id = ?1 and c.user.userName like %?2% and lower(c.content) like %?3% and c.comment is null ")
    long countAllByFilmIdAndUser(Long filmId, String userName, String search);

    @Query("select count (c.id) from CommentEntity c where c.user.userName like %?1% and (lower(c.content) like %?2% or lower(c.film.name) like %?2% or lower(c.film.code) like %?2%) and c.comment is null")
    long countAllByUserAndProperties(String userName, String search);

    @Query("select c from CommentEntity c where c.comment.id = ?1 and c.user.userName like %?2% and lower(c.content) like %?3% and c.film.employee.userName = ?4")
    List<CommentEntity> findAllByCommentIdAndUser(Long commentId, String userName, String search, String employeeName, Pageable pageable);

    @Query("select c from CommentEntity c where c.film.id = ?1 and c.user.userName like %?2% and lower(c.content) like %?3% and c.film.employee.userName = ?4 and c.comment is null")
    List<CommentEntity> findAllByFilmIdAndUser(Long filmId, String userName, String search, String employeeName, Pageable pageable);

    @Query("select c from CommentEntity c where c.user.userName like %?1% and (lower(c.content) like %?2% or lower(c.film.name) like %?2% or lower(c.film.code) like %?2%) and c.film.employee.userName = ?3 and c.comment is null")
    List<CommentEntity> findAllByUserAndProperties(String userName, String search, String employeeName, Pageable pageable);

    @Query("select count (c.id) from CommentEntity c where c.comment.id = ?1 and c.user.userName like %?2% and lower(c.content) like %?3% and c.film.employee.userName = ?4")
    long countAllByCommentIdAndUser(Long commentId, String userName, String search, String employeeName);

    @Query("select count (c.id)from CommentEntity c where c.film.id = ?1 and c.user.userName like %?2% and lower(c.content) like %?3% and c.film.employee.userName = ?4 and c.comment is null")
    long countAllByFilmIdAndUser(Long filmId, String userName, String search, String employeeName);

    @Query("select count (c.id) from CommentEntity c where c.user.userName like %?1% and (lower(c.content) like %?2% or lower(c.film.name) like %?2% or lower(c.film.code) like %?2%) and c.film.employee.userName = ?3 and c.comment is null")
    long countAllByUserAndProperties(String userName, String search, String employeeName);

    List<CommentEntity> findAllByComment_IdAndFilm_Employee_UserName(Long commentId, String userName);

    List<CommentEntity> findAllByComment_Id(Long commentId);
}
