package com.movie.core.dto;

public class CommentVideoLikeDTO extends AbstractDTO<CommentVideoLikeDTO> {
    private Integer status;
    private UserDTO user;
    private CommentVideoDTO commentVideo;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public CommentVideoDTO getCommentVideo() {
        return commentVideo;
    }

    public void setCommentVideo(CommentVideoDTO commentVideo) {
        this.commentVideo = commentVideo;
    }
}
