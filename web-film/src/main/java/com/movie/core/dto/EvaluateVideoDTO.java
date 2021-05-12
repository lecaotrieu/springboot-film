package com.movie.core.dto;

public class EvaluateVideoDTO extends AbstractDTO<EvaluateVideoDTO> {
    private Integer liked;
    private UserDTO user;
    private VideoDTO video;

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

    public Integer getLiked() {
        return liked;
    }

    public void setLiked(Integer liked) {
        this.liked = liked;
    }
}
