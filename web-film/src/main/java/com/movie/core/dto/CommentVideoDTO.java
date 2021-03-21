package com.movie.core.dto;

import java.util.List;

public class CommentVideoDTO extends AbstractDTO<CommentVideoDTO> {
    private String content;
    private Integer totalLike;
    private VideoDTO video;
    private UserDTO user;
    private List<CommentVideoLikeDTO> commentVideoLikes;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getTotalLike() {
        return totalLike;
    }

    public void setTotalLike(Integer totalLike) {
        this.totalLike = totalLike;
    }

    public VideoDTO getVideo() {
        return video;
    }

    public void setVideo(VideoDTO video) {
        this.video = video;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public List<CommentVideoLikeDTO> getCommentVideoLikes() {
        return commentVideoLikes;
    }

    public void setCommentVideoLikes(List<CommentVideoLikeDTO> commentVideoLikes) {
        this.commentVideoLikes = commentVideoLikes;
    }
}
