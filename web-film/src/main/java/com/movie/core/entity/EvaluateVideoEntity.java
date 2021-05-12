package com.movie.core.entity;

import javax.persistence.*;

@Entity
@Table(name = "evaluatevideo")
public class EvaluateVideoEntity extends BaseEntity {
    @Column
    private Integer liked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id")
    private VideoEntity video;


    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public VideoEntity getVideo() {
        return video;
    }

    public void setVideo(VideoEntity video) {
        this.video = video;
    }

    public Integer getLiked() {
        return liked;
    }

    public void setLiked(Integer liked) {
        this.liked = liked;
    }
}
