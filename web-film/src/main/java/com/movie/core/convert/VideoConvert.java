package com.movie.core.convert;

import com.movie.core.constant.CoreConstant;
import com.movie.core.dto.VideoDTO;
import com.movie.core.entity.UserEntity;
import com.movie.core.entity.VideoEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VideoConvert {
    public VideoDTO toDTO(VideoEntity entity) {
        VideoDTO dto = new VideoDTO();
        BeanUtils.copyProperties(entity, dto);
        if (dto.getVideo() != null && !dto.getVideo().isEmpty()) {
            dto.setVideoUrl2("https://www.googleapis.com/drive/v3/files/" + dto.getVideo() + "?alt=media&amp;&key=" + CoreConstant.DRIVE_API_KEY);
            dto.setVideoUrl("https://drive.google.com/uc?id=" + dto.getVideo());
            dto.setVideoUrl3("https://drive.google.com/file/d/" + dto.getVideo() + "/preview");
        }

        if (dto.getImage() != null && !dto.getImage().isEmpty()) {
            dto.setImageUrl("/fileUpload/" + CoreConstant.VIDEO_IMAGES + "/" + dto.getId() + "/" + dto.getImage());
        }

        if (entity.getCommentVideos() != null) {
            dto.setTotalComment(entity.getCommentVideos().size());
        }
        if (entity.getUser() != null) {
            dto.setUser(userConvert.toDTO(entity.getUser()));
        }
        return dto;
    }

    public VideoEntity toEntity(VideoDTO dto) {
        VideoEntity entity = new VideoEntity();
        BeanUtils.copyProperties(dto, entity);
        if (dto.getUser() != null) {
            UserEntity userEntity = userConvert.toEntity(dto.getUser());
            entity.setUser(userEntity);
        }
        return entity;
    }

    @Autowired
    private UserConvert userConvert;

    public VideoEntity toEntity(VideoDTO dto, VideoEntity entityOld) {
        if (dto.getVideo() != null && !dto.getVideo().isEmpty()) {
            entityOld.setVideo(dto.getVideo());
        }
        if (dto.getImage() != null && !dto.getImage().isEmpty()) {
            entityOld.setImage(dto.getImage());
        }
        entityOld.setStatus(dto.getStatus());
        entityOld.setName(dto.getName());
        entityOld.setBrief(dto.getBrief());
        if (dto.getUser() != null) {
            UserEntity userEntity = userConvert.toEntity(dto.getUser());
            entityOld.setUser(userEntity);
        }
        return entityOld;
    }
}
