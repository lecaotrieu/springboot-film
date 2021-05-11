package com.movie.core.service;

import com.movie.core.dto.CommentVideoDTO;

import java.util.List;

public interface ICommentVideoService {
    List<CommentVideoDTO> findByProperties(Long videoId, Long commentId, String search, String userName, int page, int limit, String sortExpression, String sortDirection);

    List<CommentVideoDTO> findByProperties(Long commentId, Long videoId, int page, int limit, String sortExpression, String sortDirection);

    int totalComment(Long videoId, Long commentId, String search, String userName);

    List<CommentVideoDTO> findAllByCommentId(Long commentId);

    void deleteCommentVideo(Long[] ids);

    CommentVideoDTO findOneById(Long commentId);

    int totalComment(Long id);

    Long save(CommentVideoDTO commentVideoDTO) throws Exception;

    int totalComment(Long commentId, Long videoId);
}
