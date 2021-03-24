package com.movie.core.service;

import com.movie.core.dto.CommentDTO;

import java.util.List;

public interface ICommentService {
    List<CommentDTO> findByProperties(Long filmId, int page, int limit, String sortExpression, String sortDirection);

    List<CommentDTO> findByProperties(Long filmId, Long commentId, String search, String userName, int page, int limit, String sortExpression, String sortDirection);

    List<CommentDTO> findByProperties(Long filmId, Long commentId, String search, String userName, String employeeName, int page, int limit, String sortExpression, String sortDirection);

    List<CommentDTO> findByProperties(Long commentId, Long filmId, int page, int limit, String sortExpression, String sortDirection);

    List<CommentDTO> findAllByCommentId(Long commentId, String employeeUserName);

    List<CommentDTO> findAllByCommentId(Long commentId);

    CommentDTO findOneById(Long commentId);
    void save(CommentDTO commentDTO);

    int totalComment(Long filmId);

    int totalComment(Long commentId, Long filmId);

    int totalComment(Long filmId, Long commentId, String search, String userName);

    int totalComment(Long filmId, Long commentId, String search, String employeeName, String userName);

    void deleteComment(Long[] ids);
}
