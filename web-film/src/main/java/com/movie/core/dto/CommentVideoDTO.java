package com.movie.core.dto;

import java.util.List;

public class CommentVideoDTO extends AbstractDTO<CommentVideoDTO> {
    private String content;
    private Integer totalLike;
    private VideoDTO video;
    private UserDTO user;
    private List<CommentVideoDTO> commentVideos;
    private CommentVideoDTO commentVideo;
    private List<CommentVideoLikeDTO> commentVideoLikes;
    private String thoiGianDang;
    private Integer like;
    private Integer subCommentCount=0;

    public List<CommentVideoDTO> getCommentVideos() {
        return commentVideos;
    }

    public void setCommentVideos(List<CommentVideoDTO> commentVideos) {
        this.commentVideos = commentVideos;
    }

    public CommentVideoDTO getCommentVideo() {
        return commentVideo;
    }

    public void setCommentVideo(CommentVideoDTO commentVideo) {
        this.commentVideo = commentVideo;
    }

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

    public String getThoiGianDang() {
        return thoiGianDang;
    }

    public void setThoiGianDang(String thoiGianDang) {
        this.thoiGianDang = thoiGianDang;
    }

    public Integer getSubCommentCount() {
        return subCommentCount;
    }

    public void setSubCommentCount(Integer subCommentCount) {
        this.subCommentCount = subCommentCount;
    }

    public Integer getLike() {
        return like;
    }

    public void setLike(Integer like) {
        this.like = like;
    }
}
