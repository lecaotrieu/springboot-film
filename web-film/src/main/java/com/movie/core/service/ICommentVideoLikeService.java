package com.movie.core.service;

import com.movie.core.dto.CommentVideoLikeDTO;

public interface ICommentVideoLikeService {
    CommentVideoLikeDTO findByUserAndComment(Long id, Long commentId);

    void save(CommentVideoLikeDTO dto);
}
