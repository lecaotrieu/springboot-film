package com.movie.core.convert;

import com.movie.core.dto.EpisodeDTO;
import com.movie.core.entity.EpisodeEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EpisodeConvert {
    @Autowired
    private FilmConvert filmConvert;
    public EpisodeDTO toDTO(EpisodeEntity entity) {
        EpisodeDTO dto = new EpisodeDTO();
        BeanUtils.copyProperties(entity, dto);
        if (entity.getFilm() != null) {
            dto.setFilm(filmConvert.toDTO(entity.getFilm()));
        }
        if (dto.getEpisodeId() != null && !dto.getEpisodeId().isEmpty()) {
            dto.setVideoUrl("https://drive.google.com/uc?id=" + dto.getEpisodeId());
        }
        return dto;
    }


    public EpisodeEntity toEntity(EpisodeDTO dto) {
        EpisodeEntity entity = new EpisodeEntity();
        BeanUtils.copyProperties(dto, entity);
        if (dto.getFilm() != null) {
            entity.setFilm(filmConvert.toEntity(dto.getFilm()));
        }
        return entity;
    }
}
