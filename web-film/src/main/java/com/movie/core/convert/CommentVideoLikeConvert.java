package com.movie.core.convert;

import com.movie.core.dto.CommentLikeDTO;
import com.movie.core.dto.CommentVideoLikeDTO;
import com.movie.core.entity.CommentLikeEntity;
import com.movie.core.entity.CommentVideoLikeEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommentVideoLikeConvert {
    @Autowired
    private CommentVideoConvert commentVideoConvert;
    @Autowired
    private UserConvert userConvert;

    public CommentVideoLikeDTO toDTO(CommentVideoLikeEntity entity) {
        CommentVideoLikeDTO dto = new CommentVideoLikeDTO();
        BeanUtils.copyProperties(entity, dto);
        if (entity.getCommentVideo() != null) {
            dto.setCommentVideo(commentVideoConvert.toDTO(entity.getCommentVideo()));
        }
        if (entity.getUser() != null) {
            dto.setUser(userConvert.toDTO(entity.getUser()));
        }
        return dto;
    }


    public CommentVideoLikeEntity toEntity(CommentVideoLikeDTO dto) {
        CommentVideoLikeEntity entity = new CommentVideoLikeEntity();
        BeanUtils.copyProperties(dto, entity);
        if (dto.getCommentVideo() != null) {
            entity.setCommentVideo(commentVideoConvert.toEntity(dto.getCommentVideo()));
        }
        if (dto.getUser() != null) {
            entity.setUser(userConvert.toEntity(dto.getUser()));
        }
        return entity;
    }
}
