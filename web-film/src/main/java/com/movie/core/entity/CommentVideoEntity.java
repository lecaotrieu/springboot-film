package com.movie.core.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "commentvideo")
public class CommentVideoEntity extends BaseEntity {
    @Column
    private String content;

    @Column(name = "totallike")
    private Integer totalLike;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id")
    private VideoEntity video;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(mappedBy = "commentVideo", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<CommentVideoLikeEntity> commentVideoLikes;

    @OneToMany(mappedBy = "commentVideo", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<CommentVideoEntity> commentVideos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private CommentVideoEntity commentVideo;

    public List<CommentVideoEntity> getCommentVideos() {
        return commentVideos;
    }

    public void setCommentVideos(List<CommentVideoEntity> commentVideos) {
        this.commentVideos = commentVideos;
    }

    public CommentVideoEntity getCommentVideo() {
        return commentVideo;
    }

    public void setCommentVideo(CommentVideoEntity commentVideo) {
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

    public VideoEntity getVideo() {
        return video;
    }

    public void setVideo(VideoEntity video) {
        this.video = video;
    }

    public List<CommentVideoLikeEntity> getCommentVideoLikes() {
        return commentVideoLikes;
    }

    public void setCommentVideoLikes(List<CommentVideoLikeEntity> commentVideoLikes) {
        this.commentVideoLikes = commentVideoLikes;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
