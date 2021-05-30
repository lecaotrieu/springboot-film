package com.movie.core.dto;

import java.util.List;

public class VideoDTO extends AbstractDTO<VideoDTO> {
    private String code;
    private String name;
    private String video;
    private String image;
    private String imageUrl;
    private String videoUrl;
    private String videoUrl2;
    private String videoUrl3;
    private Integer status;
    private Integer view;
    private String brief;
    private Integer totalLike;
    private Integer totalComment;
    private UserDTO user;
    private List<CommentVideoDTO> commentVideos;
    private List<EvaluateVideoDTO> evaluateVideos;

    public List<CommentVideoDTO> getCommentVideos() {
        return commentVideos;
    }

    public void setCommentVideos(List<CommentVideoDTO> commentVideos) {
        this.commentVideos = commentVideos;
    }

    public List<EvaluateVideoDTO> getEvaluateVideos() {
        return evaluateVideos;
    }

    public void setEvaluateVideos(List<EvaluateVideoDTO> evaluateVideos) {
        this.evaluateVideos = evaluateVideos;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<CommentVideoDTO> getCommentVideoS() {
        return commentVideoS;
    }

    public void setCommentVideoS(List<CommentVideoDTO> commentVideoS) {
        this.commentVideoS = commentVideoS;
    }

    private List<CommentVideoDTO> commentVideoS;

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

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Integer getTotalComment() {
        return totalComment;
    }

    public void setTotalComment(Integer totalComment) {
        this.totalComment = totalComment;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public Integer getView() {
        return view;
    }

    public void setView(Integer view) {
        this.view = view;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public Integer getTotalLike() {
        return totalLike;
    }

    public void setTotalLike(Integer totalLike) {
        this.totalLike = totalLike;
    }

    public String getVideoUrl3() {
        return videoUrl3;
    }

    public void setVideoUrl3(String videoUrl3) {
        this.videoUrl3 = videoUrl3;
    }

    public String getVideoUrl2() {
        return videoUrl2;
    }

    public void setVideoUrl2(String videoUrl2) {
        this.videoUrl2 = videoUrl2;
    }
}
