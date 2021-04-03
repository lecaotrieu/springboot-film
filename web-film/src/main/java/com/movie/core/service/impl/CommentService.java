package com.movie.core.service.impl;

import com.movie.core.dto.CommentDTO;
import com.movie.core.entity.CommentEntity;
import com.movie.core.repository.CommentRepository;
import com.movie.core.service.ICommentService;
import com.movie.core.convert.CommentConvert;
import com.movie.core.service.utils.PagingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService implements ICommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CommentConvert commentConvert;
    @Autowired
    private PagingUtils pagingUtils;

    public List<CommentDTO> findByProperties(Long filmId, int page, int limit, String sortExpression, String sortDirection) {
        Pageable pageable = pagingUtils.setPageable(page, limit, sortExpression, sortDirection);
        List<CommentEntity> commentEntities = commentRepository.findAllByFilm_IdAndCommentIsNull(filmId, pageable);
        List<CommentDTO> commentDTOS = new ArrayList<CommentDTO>();
        for (CommentEntity entity : commentEntities) {
            CommentDTO commentDTO = commentConvert.toDTO(entity);
            commentDTO.setSubCommentCount(entity.getComments().size());
            commentDTOS.add(commentDTO);
        }
        return commentDTOS;
    }

    @Override
    public List<CommentDTO> findByProperties(Long filmId, Long commentId, String search, String userName, int page, int limit, String sortExpression, String sortDirection) {
        Pageable pageable = pagingUtils.setPageable(page, limit, sortExpression, sortDirection);
        search = search.toLowerCase();
        List<CommentEntity> commentEntities;
        if (commentId == null) {
            if (filmId == null) {
                commentEntities = commentRepository.findAllByUserAndProperties(userName, search, pageable);
            } else {
                commentEntities = commentRepository.findAllByFilmIdAndUser(filmId, userName, search, pageable);
            }
        } else {
            commentEntities = commentRepository.findAllByCommentIdAndUser(commentId, userName, search, pageable);
        }
        List<CommentDTO> commentDTOS = new ArrayList<CommentDTO>();
        for (CommentEntity entity : commentEntities) {
            CommentDTO commentDTO = commentConvert.toDTO(entity);
            commentDTO.setSubCommentCount(entity.getComments().size());
            commentDTOS.add(commentDTO);
        }
        return commentDTOS;
    }

    @Override
    public List<CommentDTO> findByProperties(Long filmId, Long commentId, String search, String userName, String employeeName, int page, int limit, String sortExpression, String sortDirection) {
        Pageable pageable = pagingUtils.setPageable(page, limit, sortExpression, sortDirection);
        search = search.toLowerCase();
        List<CommentEntity> commentEntities;
        if (commentId == null) {
            if (filmId == null) {
                commentEntities = commentRepository.findAllByUserAndProperties(userName, search, employeeName, pageable);
            } else {
                commentEntities = commentRepository.findAllByFilmIdAndUser(filmId, userName, search, employeeName, pageable);
            }
        } else {
            commentEntities = commentRepository.findAllByCommentIdAndUser(commentId, userName, search, employeeName, pageable);
        }
        List<CommentDTO> commentDTOS = new ArrayList<CommentDTO>();
        for (CommentEntity entity : commentEntities) {
            CommentDTO commentDTO = commentConvert.toDTO(entity);
            commentDTO.setSubCommentCount(entity.getComments().size());
            commentDTOS.add(commentDTO);
        }
        return commentDTOS;
    }

    public List<CommentDTO> findByProperties(Long commentId, Long filmId, int page, int limit, String sortExpression, String sortDirection) {
        Pageable pageable = pagingUtils.setPageable(page, limit, sortExpression, sortDirection);
        List<CommentEntity> commentEntities = commentRepository.findAllByComment_IdAndFilm_Id(commentId, filmId, pageable);
        List<CommentDTO> commentDTOS = new ArrayList<CommentDTO>();
        for (CommentEntity entity : commentEntities) {
            CommentDTO commentDTO = commentConvert.toDTO(entity);
            commentDTOS.add(commentDTO);
        }
        return commentDTOS;
    }

    @Override
    public List<CommentDTO> findAllByCommentId(Long commentId, String employeeUserName) {
        List<CommentEntity> commentEntities = commentRepository.findAllByComment_IdAndFilm_Employee_UserName(commentId, employeeUserName);
        List<CommentDTO> commentDTOS = new ArrayList<CommentDTO>();
        for (CommentEntity entity : commentEntities) {
            CommentDTO commentDTO = commentConvert.toDTO(entity);
            commentDTOS.add(commentDTO);
        }
        return commentDTOS;
    }

    @Override
    public List<CommentDTO> findAllByCommentId(Long commentId) {
        List<CommentEntity> commentEntities = commentRepository.findAllByComment_Id(commentId);
        List<CommentDTO> commentDTOS = new ArrayList<CommentDTO>();
        for (CommentEntity entity : commentEntities) {
            CommentDTO commentDTO = commentConvert.toDTO(entity);
            commentDTOS.add(commentDTO);
        }
        return commentDTOS;
    }

    @Override
    public CommentDTO findOneById(Long commentId) {
        CommentEntity commentEntity = commentRepository.getOne(commentId);
        return commentConvert.toDTO(commentEntity);
    }

    @Transactional
    public Long save(CommentDTO commentDTO) throws Exception {
        CommentEntity entity;
        if (commentDTO.getId() != null) {
            entity = commentRepository.getOne(commentDTO.getId());
            entity.setContent(commentDTO.getContent());
        } else {
            entity = commentConvert.toEntity(commentDTO);
            entity.setTotalLike(0);
        }
        entity = commentRepository.save(entity);
        return entity.getId();
    }

    public int totalComment(Long filmId) {
        int result = (int) commentRepository.countAllByFilm_Id(filmId);
        return result;
    }

    public int totalComment(Long commentId, Long filmId) {
        int result = (int) commentRepository.countAllByFilm_IdAndComment_Id(filmId, commentId);
        return result;
    }

    @Override
    public int totalComment(Long filmId, Long commentId, String search, String userName) {
        int rs = 0;
        if (commentId == null) {
            rs = (int) commentRepository.countAllByUserAndProperties(userName, search);
        } else {
            if (filmId == null) {
                rs = (int) commentRepository.countAllByCommentIdAndUser(commentId, userName, search);
            } else {
                rs = (int) commentRepository.countAllByFilmIdAndUser(filmId, userName, search);
            }
        }
        return rs;
    }

    @Override
    public int totalComment(Long filmId, Long commentId, String search, String employeeName, String userName) {
        int rs = 0;
        if (commentId == null) {
            rs = (int) commentRepository.countAllByUserAndProperties(userName, search, employeeName);
        } else {
            if (filmId == null) {
                rs = (int) commentRepository.countAllByCommentIdAndUser(commentId, userName, search, employeeName);
            } else {
                rs = (int) commentRepository.countAllByFilmIdAndUser(filmId, userName, search, employeeName);
            }
        }
        return rs;
    }

    @Transactional
    public void deleteComment(Long[] ids) {
        for (Long id : ids) {
            commentRepository.deleteById(id);
        }
    }
}
