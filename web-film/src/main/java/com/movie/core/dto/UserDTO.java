package com.movie.core.dto;

import java.sql.Date;
import java.util.List;

public class UserDTO extends AbstractDTO<UserDTO> {
    private String userName;
    private String password;
    private String confirmPassword;
    private String newPassword;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private Integer status;
    private String photo;
    private String photoUrl;
    private Date birthDate;
    private Integer totalFollow;
    private String address;
    private List<CommentVideoDTO> commentVideos;
    private List<CommentVideoLikeDTO> commentVideoLikes;
    private List<EvaluateDTO> evaluates;
    private List<CommentDTO> comments;
    private List<CommentLikeDTO> commentLikes;
    private List<VideoDTO> videos;
    private List<UserDTO> usersFollowed;
    private List<UserDTO> followers;
    private List<EvaluateVideoDTO> evaluateVideos;
    private List<SubscribeDTO> subscribes;
    private List<SubscribeDTO> beSubscribes;

    public List<SubscribeDTO> getSubscribes() {
        return subscribes;
    }

    public void setSubscribes(List<SubscribeDTO> subscribes) {
        this.subscribes = subscribes;
    }

    public List<SubscribeDTO> getBeSubscribes() {
        return beSubscribes;
    }

    public void setBeSubscribes(List<SubscribeDTO> beSubscribes) {
        this.beSubscribes = beSubscribes;
    }

    public List<CommentVideoDTO> getCommentVideos() {
        return commentVideos;
    }

    public void setCommentVideos(List<CommentVideoDTO> commentVideos) {
        this.commentVideos = commentVideos;
    }

    public List<CommentVideoLikeDTO> getCommentVideoLikes() {
        return commentVideoLikes;
    }

    public void setCommentVideoLikes(List<CommentVideoLikeDTO> commentVideoLikes) {
        this.commentVideoLikes = commentVideoLikes;
    }

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

    public List<EvaluateDTO> getEvaluates() {
        return evaluates;
    }

    public void setEvaluates(List<EvaluateDTO> evaluates) {
        this.evaluates = evaluates;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }

    public List<CommentLikeDTO> getCommentLikes() {
        return commentLikes;
    }

    public void setCommentLikes(List<CommentLikeDTO> commentLikes) {
        this.commentLikes = commentLikes;
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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public List<VideoDTO> getVideos() {
        return videos;
    }

    public void setVideos(List<VideoDTO> videos) {
        this.videos = videos;
    }

    public List<UserDTO> getUsersFollowed() {
        return usersFollowed;
    }

    public void setUsersFollowed(List<UserDTO> usersFollowed) {
        this.usersFollowed = usersFollowed;
    }

    public List<UserDTO> getFollowers() {
        return followers;
    }

    public void setFollowers(List<UserDTO> followers) {
        this.followers = followers;
    }

    public List<EvaluateVideoDTO> getEvaluateVideos() {
        return evaluateVideos;
    }

    public void setEvaluateVideos(List<EvaluateVideoDTO> evaluateVideos) {
        this.evaluateVideos = evaluateVideos;
    }

    public Integer getTotalFollow() {
        return totalFollow;
    }

    public void setTotalFollow(Integer totalFollow) {
        this.totalFollow = totalFollow;
    }
}
