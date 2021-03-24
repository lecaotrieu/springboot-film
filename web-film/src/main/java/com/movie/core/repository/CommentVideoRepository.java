package com.movie.core.repository;

import com.movie.core.entity.CommentEntity;
import com.movie.core.entity.CommentVideoEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentVideoRepository extends JpaRepository<CommentVideoEntity, Long> {
    @Query("select c from CommentVideoEntity c where c.commentVideo.id = ?1 and c.user.userName like %?2% and lower(c.content) like %?3%")
    List<CommentVideoEntity> findAllByCommentIdAndUser(Long commentId, String userName, String search, Pageable pageable);

    @Query("select c from CommentVideoEntity c where c.video.id = ?1 and c.user.userName like %?2% and lower(c.content) like %?3% and c.commentVideo is null")
    List<CommentVideoEntity> findAllByVideoIdAndUser(Long videoId, String userName, String search, Pageable pageable);

    @Query("select c from CommentVideoEntity c where c.user.userName like %?1% and (lower(c.content) like %?2% or lower(c.video.name) like %?2% or lower(c.video.code) like %?2%) and c.commentVideo is null")
    List<CommentVideoEntity> findAllByUserAndProperties(String userName, String search, Pageable pageable);

    @Query("select count (c.id) from CommentVideoEntity c where c.commentVideo.id = ?1 and c.user.userName like %?2% and lower(c.content) like %?3%")
    long countAllByCommentIdAndUser(Long commentId, String userName, String search);

    @Query("select count (c.id)from CommentVideoEntity c where c.video.id = ?1 and c.user.userName like %?2% and lower(c.content) like %?3% and c.commentVideo is null ")
    long countAllByVideoIdAndUser(Long videoId, String userName, String search);

    @Query("select count (c.id) from CommentVideoEntity c where c.user.userName like %?1% and (lower(c.content) like %?2% or lower(c.video.name) like %?2% or lower(c.video.code) like %?2%) and c.commentVideo is null")
    long countAllByUserAndProperties(String userName, String search);

    List<CommentVideoEntity> findAllByCommentVideo_Id(Long commentId);
}
