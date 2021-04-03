package com.movie.core.service.impl;

import com.movie.core.convert.CommentLikeConvert;
import com.movie.core.dto.CommentLikeDTO;
import com.movie.core.entity.CommentEntity;
import com.movie.core.entity.CommentLikeEntity;
import com.movie.core.repository.CommentLikeRepository;
import com.movie.core.repository.CommentRepository;
import com.movie.core.service.ICommentLikeService;
import com.movie.core.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentLikeService implements ICommentLikeService {
    @Autowired
    private CommentLikeRepository commentLikeRepository;
    @Autowired
    private CommentLikeConvert convert;

    public CommentLikeDTO findByUserAndComment(Long userId, Long commentId) {
        CommentLikeEntity entity = commentLikeRepository.findByUser_IdAndComment_Id(userId, commentId);
        if (entity != null) {
            return convert.toDTO(entity);
        }
        return null;
    }

    public int totalCommentLike(Long commentId) {
        int result = (int) commentLikeRepository.countAllByComment_IdAndStatus(commentId, 1);
        return result;
    }


    @Autowired
    private CommentRepository commentRepository;

    @Transactional
    public void save(CommentLikeDTO dto) {
        CommentLikeEntity entity = commentLikeRepository.findByUser_IdAndComment_Id(dto.getUser().getId(), dto.getComment().getId());
        if (entity == null) {
            commentLikeRepository.save(convert.toEntity(dto));
        } else {
            CommentLikeEntity entity2 = convert.toEntity(dto);
            entity2.setId(entity.getId());
            commentLikeRepository.save(entity2);
        }
        CommentEntity commentEntity = commentRepository.getOne(dto.getComment().getId());
        commentEntity.setTotalLike((int) commentLikeRepository.countAllByComment_IdAndStatus(commentEntity.getId(), 1));
        commentRepository.save(commentEntity);
    }
}
