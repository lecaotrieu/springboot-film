package com.movie.core.repository;

import com.movie.core.entity.CommentVideoLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentVideoLikeRepository extends JpaRepository<CommentVideoLikeEntity, Long> {
    CommentVideoLikeEntity findByUser_IdAndCommentVideo_Id(Long userId, Long commentId);
    long countAllByCommentVideo_Id(Long commentId);

    long countAllByCommentVideo_IdAndStatus(Long id, int status);
}
