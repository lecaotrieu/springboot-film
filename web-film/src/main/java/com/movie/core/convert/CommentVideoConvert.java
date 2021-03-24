package com.movie.core.convert;

import com.movie.core.dto.CommentDTO;
import com.movie.core.dto.CommentVideoDTO;
import com.movie.core.entity.CommentEntity;
import com.movie.core.entity.CommentVideoEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

@Component
public class CommentVideoConvert {
    @Autowired
    private UserConvert userConvert;

    @Autowired
    private VideoConvert videoConvert;

    public CommentVideoDTO toDTO(CommentVideoEntity entity) {
        CommentVideoDTO dto = new CommentVideoDTO();
        BeanUtils.copyProperties(entity, dto);
        if (entity.getUser() != null) {
            dto.setUser(userConvert.toDTO(entity.getUser()));
        }
        if (entity.getVideo() != null) {
            dto.setVideo(videoConvert.toDTO(entity.getVideo()));
        }
        if (entity.getCommentVideo()!=null){
            dto.setCommentVideo(toDTO(entity.getCommentVideo()));
        }
        if (entity.getCreatedDate()!=null){
            Long time = System.currentTimeMillis() - entity.getCreatedDate().getTime();
            if (TimeUnit.HOURS.convert(time, TimeUnit.MILLISECONDS) > 72) {
                DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                dto.setThoiGianDang(df.format(entity.getCreatedDate()));
            } else if (TimeUnit.HOURS.convert(time, TimeUnit.MILLISECONDS) > 24) {
                dto.setThoiGianDang(TimeUnit.DAYS.convert(time, TimeUnit.MILLISECONDS) + " ngày trước");
            } else if (TimeUnit.MINUTES.convert(time, TimeUnit.MILLISECONDS) > 60) {
                dto.setThoiGianDang(TimeUnit.HOURS.convert(time, TimeUnit.MILLISECONDS) + " giờ trước");
            } else if (TimeUnit.SECONDS.convert(time, TimeUnit.MILLISECONDS) > 60) {
                dto.setThoiGianDang(TimeUnit.MINUTES.convert(time, TimeUnit.MILLISECONDS) + " phút trước");
            } else {
                dto.setThoiGianDang(TimeUnit.SECONDS.convert(time, TimeUnit.MILLISECONDS) + " giây trước");
            }
        }
        return dto;
    }


    public CommentVideoEntity toEntity(CommentVideoDTO dto) {
        CommentVideoEntity entity = new CommentVideoEntity();
        BeanUtils.copyProperties(dto, entity);
        if (dto.getUser() != null) {
            entity.setUser(userConvert.toEntity(dto.getUser()));
        }
        if (dto.getCommentVideo()!=null){
            entity.setCommentVideo(toEntity(dto.getCommentVideo()));
        }
        if (dto.getVideo() != null) {
            entity.setVideo(videoConvert.toEntity(dto.getVideo()));
        }
        return entity;
    }
}
