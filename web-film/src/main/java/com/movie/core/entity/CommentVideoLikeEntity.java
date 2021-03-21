package com.movie.core.entity;

import javax.persistence.*;

@Entity
@Table(name = "commentvideo_like")
public class CommentVideoLikeEntity extends BaseEntity {
    @Column
    private Integer status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commentvideo_id")
    private CommentVideoEntity commentVideo;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public CommentVideoEntity getCommentVideo() {
        return commentVideo;
    }

    public void setCommentVideo(CommentVideoEntity commentVideo) {
        this.commentVideo = commentVideo;
    }
}
