package com.movie.core.dto;

import java.util.List;

public class CategoryDTO extends AbstractDTO<CategoryDTO>{
    private String code;
    private String name;
    private String description;
    private String avatar;
    private Integer totalFilm;
    private List<FilmDTO> films;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

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

    public Integer getTotalFilm() {
        return totalFilm;
    }

    public void setTotalFilm(Integer totalFilm) {
        this.totalFilm = totalFilm;
    }
}
