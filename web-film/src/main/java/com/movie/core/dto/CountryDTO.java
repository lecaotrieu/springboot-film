package com.movie.core.dto;

import java.util.List;

public class CountryDTO extends AbstractDTO<CountryDTO> {
    private String code;
    private String name;
    private Integer totalFilm;
    private List<FilmDTO> films;
    private List<EmployeeDTO> employees;

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

    public List<FilmDTO> getFilms() {
        return films;
    }

    public void setFilms(List<FilmDTO> films) {
        this.films = films;
    }

    public List<EmployeeDTO> getEmployees() {
        return employees;
    }

    public void setEmployees(List<EmployeeDTO> employees) {
        this.employees = employees;
    }

    public Integer getTotalFilm() {
        return totalFilm;
    }

    public void setTotalFilm(Integer totalFilm) {
        this.totalFilm = totalFilm;
    }
}
