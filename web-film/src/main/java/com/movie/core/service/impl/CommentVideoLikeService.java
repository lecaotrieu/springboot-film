package com.movie.core.service.impl;

import com.movie.core.convert.CommentVideoLikeConvert;
import com.movie.core.dto.CommentVideoLikeDTO;
import com.movie.core.entity.CommentVideoEntity;
import com.movie.core.entity.CommentVideoLikeEntity;
import com.movie.core.repository.CommentVideoLikeRepository;
import com.movie.core.repository.CommentVideoRepository;
import com.movie.core.service.ICommentVideoLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentVideoLikeService implements ICommentVideoLikeService {
    @Autowired
    private CommentVideoLikeRepository commentVideoLikeRepository;
    @Autowired
    private CommentVideoRepository commentVideoRepository;
    @Autowired
    private CommentVideoLikeConvert convert;

    @Override
    public CommentVideoLikeDTO findByUserAndComment(Long userId, Long commentId) {
        CommentVideoLikeEntity entity = commentVideoLikeRepository.findByUser_IdAndCommentVideo_Id(userId, commentId);
        if (entity != null) {
            return convert.toDTO(entity);
        }
        return null;
    }

    @Transactional
    public void save(CommentVideoLikeDTO dto) {
        CommentVideoLikeEntity entity = commentVideoLikeRepository.findByUser_IdAndCommentVideo_Id(dto.getUser().getId(), dto.getCommentVideo().getId());
        if (entity == null) {
            commentVideoLikeRepository.save(convert.toEntity(dto));
        } else {
            CommentVideoLikeEntity entity2 = convert.toEntity(dto);
            entity2.setId(entity.getId());
            commentVideoLikeRepository.save(entity2);
        }
        CommentVideoEntity commentVideoEntity = commentVideoRepository.getOne(dto.getCommentVideo().getId());
        commentVideoEntity.setTotalLike((int) commentVideoLikeRepository.countAllByCommentVideo_IdAndStatus(commentVideoEntity.getId(), 1));
        commentVideoRepository.save(commentVideoEntity);
    }
}
