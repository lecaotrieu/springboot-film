package com.movie.core.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "video")
public class VideoEntity extends BaseEntity {
    @Column
    private String code;
    @Column
    private String name;
    @Column
    private String video;
    @Column
    private String brief;
    @Column
    private String image;
    @Column
    private Integer view;
    @Column
    private Integer status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "video")
    private List<CommentVideoEntity> commentVideos;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "video")
    private List<EvaluateVideoEntity> evaluateVideos;

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

    public List<CommentVideoEntity> getCommentVideos() {
        return commentVideos;
    }

    public void setCommentVideos(List<CommentVideoEntity> commentVideos) {
        this.commentVideos = commentVideos;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<EvaluateVideoEntity> getEvaluateVideos() {
        return evaluateVideos;
    }

    public void setEvaluateVideos(List<EvaluateVideoEntity> evaluateVideos) {
        this.evaluateVideos = evaluateVideos;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
}
