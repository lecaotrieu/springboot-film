package com.movie.core.entity;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "user")
public class UserEntity extends BaseEntity {
    @Column(name = "username", unique = true, nullable = false)
    private String userName;

    @Column(name = "password", nullable = false)
    private String password;

    @Column
    private String email;

    @Column(name = "totalfollow")
    private Integer totalFollow;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "photo")
    private String photo;

    @Column(name = "birthdate")
    private Date birthDate;

    @Column
    private String phone;

    @Column
    private Integer status;

    @Column
    private Float money;

    @Column(columnDefinition = "VARCHAR(255)")
    private String address;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<SubscribeEntity> subscribes;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userFollow")
    private List<SubscribeEntity> beSubscribes;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<EvaluateEntity> evaluates;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<CommentEntity> comments;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<CommentVideoEntity> commentVideos;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<CommentLikeEntity> commentLikes;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<VideoEntity> videos;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<EvaluateVideoEntity> evaluateVideos;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<CommentVideoLikeEntity> commentVideoLikes;

    @ManyToMany(mappedBy = "followers", fetch = FetchType.LAZY)
    private List<UserEntity> usersFollowed;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "subscribe", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id_follow"))
    private List<UserEntity> followers;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<EvaluateEntity> getEvaluates() {
        return evaluates;
    }

    public void setEvaluates(List<EvaluateEntity> evaluates) {
        this.evaluates = evaluates;
    }

    public List<CommentEntity> getComments() {
        return comments;
    }

    public void setComments(List<CommentEntity> comments) {
        this.comments = comments;
    }

    public List<CommentLikeEntity> getCommentLikes() {
        return commentLikes;
    }

    public void setCommentLikes(List<CommentLikeEntity> commentLikes) {
        this.commentLikes = commentLikes;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    public List<VideoEntity> getVideos() {
        return videos;
    }

    public void setVideos(List<VideoEntity> videos) {
        this.videos = videos;
    }

    public List<UserEntity> getUsersFollowed() {
        return usersFollowed;
    }

    public void setUsersFollowed(List<UserEntity> usersFollowed) {
        this.usersFollowed = usersFollowed;
    }

    public List<UserEntity> getFollowers() {
        return followers;
    }

    public void setFollowers(List<UserEntity> followers) {
        this.followers = followers;
    }

    public List<CommentVideoLikeEntity> getCommentVideoLikes() {
        return commentVideoLikes;
    }

    public void setCommentVideoLikes(List<CommentVideoLikeEntity> commentVideoLikes) {
        this.commentVideoLikes = commentVideoLikes;
    }

    public List<CommentVideoEntity> getCommentVideos() {
        return commentVideos;
    }

    public void setCommentVideos(List<CommentVideoEntity> commentVideos) {
        this.commentVideos = commentVideos;
    }

    public List<EvaluateVideoEntity> getEvaluateVideos() {
        return evaluateVideos;
    }

    public void setEvaluateVideos(List<EvaluateVideoEntity> evaluateVideos) {
        this.evaluateVideos = evaluateVideos;
    }

    public List<SubscribeEntity> getSubscribes() {
        return subscribes;
    }

    public void setSubscribes(List<SubscribeEntity> subscribes) {
        this.subscribes = subscribes;
    }

    public List<SubscribeEntity> getBeSubscribes() {
        return beSubscribes;
    }

    public void setBeSubscribes(List<SubscribeEntity> beSubscribes) {
        this.beSubscribes = beSubscribes;
    }

    public Integer getTotalFollow() {
        return totalFollow;
    }

    public void setTotalFollow(Integer totalFollow) {
        this.totalFollow = totalFollow;
    }
}
