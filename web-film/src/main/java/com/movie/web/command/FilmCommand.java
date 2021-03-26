package com.movie.web.command;

import com.movie.core.command.AbstractCommand;
import com.movie.core.dto.FilmDTO;

public class FilmCommand extends AbstractCommand<FilmDTO> {
    public FilmCommand() {
        this.pojo = new FilmDTO();
    }

    private String filmType = "";
    private String year = "";
    private String country = "";
    private String category = "";
    private String sort;

    public String getFilmType() {
        return filmType;
    }

    public void setFilmType(String filmType) {
        this.filmType = filmType;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        switch (sort) {
            case "view": {
                setSortExpression("view");
                setSortDirection("1");
                break;
            }
            case "view-d": {
                setSortExpression("view");
                setSortDirection("0");
                break;
            }
            case "scores": {
                setSortExpression("scores");
                setSortDirection("1");
                break;
            }
            case "scores-d": {
                setSortExpression("scores");
                setSortDirection("0");
                break;
            }
            case "createdDate": {
                setSortExpression("createdDate");
                setSortDirection("1");
                break;
            }
            case "createdDate-d": {
                setSortExpression("createdDate");
                setSortDirection("0");
            }
            break;
        }
        this.sort = sort;
    }
}
