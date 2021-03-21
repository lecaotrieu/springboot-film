package com.movie.web.command;

import com.movie.core.command.AbstractCommand;
import com.movie.core.dto.EpisodeDTO;
import com.movie.core.dto.FilmDTO;

public class EpisodeCommand extends AbstractCommand<EpisodeDTO> {
    public EpisodeCommand() {
        this.pojo = new EpisodeDTO();
    }

    private Long filmId;
    private FilmDTO filmDTO;

    public Long getFilmId() {
        return filmId;
    }

    public void setFilmId(Long filmId) {
        this.filmId = filmId;
    }

    public FilmDTO getFilmDTO() {
        return filmDTO;
    }

    public void setFilmDTO(FilmDTO filmDTO) {
        this.filmDTO = filmDTO;
    }
}
