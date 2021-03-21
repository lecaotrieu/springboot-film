package com.movie.core.dto;

import com.movie.core.entity.UserEntity;

import java.util.List;

public class VideoDTO extends AbstractDTO<VideoDTO> {
    private String code;
    private String name;
    private String video;
    private Integer status;
    private UserEntity user;
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

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
