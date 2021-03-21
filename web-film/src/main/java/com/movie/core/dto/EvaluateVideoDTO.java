package com.movie.core.dto;

public class EvaluateVideoDTO extends AbstractDTO<EvaluateVideoDTO> {
    private Integer like;
    private UserDTO user;
    private VideoDTO video;

    public Integer getLike() {
        return like;
    }

    public void setLike(Integer like) {
        this.like = like;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public VideoDTO getVideo() {
        return video;
    }

    public void setVideo(VideoDTO video) {
        this.video = video;
    }
}
