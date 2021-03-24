package com.movie.core.service;

import com.movie.core.dto.CommentVideoDTO;

import java.util.List;

public interface ICommentVideoService {
    List<CommentVideoDTO> findByProperties(Long videoId, Long commentId, String search, String userName, int page, int limit, String sortExpression, String sortDirection);

    int totalComment(Long videoId, Long commentId, String search, String userName);

    List<CommentVideoDTO> findAllByCommentId(Long commentId);

    void deleteCommentVideo(Long[] ids);

    CommentVideoDTO findOneById(Long commentId);

}
