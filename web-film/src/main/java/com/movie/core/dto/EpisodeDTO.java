package com.movie.core.dto;

import org.springframework.web.multipart.MultipartFile;

public class EpisodeDTO extends AbstractDTO<EpisodeDTO> {
    private String episodeId;
    private String name;
    private Integer episodeCode;
    private MultipartFile video;
    private String videoUrl;
    private String videoUrl2;
    private String videoUrl3;
    private Integer status;
    private FilmDTO film;

    public String getEpisodeId() {
        return episodeId;
    }

    public void setEpisodeId(String episodeId) {
        this.episodeId = episodeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public FilmDTO getFilm() {
        return film;
    }

    public void setFilm(FilmDTO film) {
        this.film = film;
    }

    public Integer getEpisodeCode() {
        return episodeCode;
    }

    public void setEpisodeCode(Integer episodeCode) {
        this.episodeCode = episodeCode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public MultipartFile getVideo() {
        return video;
    }

    public void setVideo(MultipartFile video) {
        this.video = video;
    }

    public String getVideoUrl2() {
        return videoUrl2;
    }

    public void setVideoUrl2(String videoUrl2) {
        this.videoUrl2 = videoUrl2;
    }

    public String getVideoUrl3() {
        return videoUrl3;
    }

    public void setVideoUrl3(String videoUrl3) {
        this.videoUrl3 = videoUrl3;
    }
}
