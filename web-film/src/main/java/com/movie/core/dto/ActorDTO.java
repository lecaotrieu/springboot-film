package com.movie.core.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ActorDTO extends AbstractDTO<ActorDTO>{
    private String name;
    private String code;
    private String description;
    private String avatar;
    private String avatarUrl;
    private List<FilmDTO> films;
    private Integer totalFilm;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<FilmDTO> getFilms() {
        return films;
    }

    public void setFilms(List<FilmDTO> films) {
        this.films = films;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getTotalFilm() {
        return totalFilm;
    }

    public void setTotalFilm(Integer totalFilm) {
        this.totalFilm = totalFilm;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
