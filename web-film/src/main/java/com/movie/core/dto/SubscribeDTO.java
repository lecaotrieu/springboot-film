package com.movie.core.dto;

public class SubscribeDTO extends AbstractDTO<SubscribeDTO> {
    private Integer follow;
    private UserDTO user;
    private UserDTO userFollow;

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public UserDTO getUserFollow() {
        return userFollow;
    }

    public void setUserFollow(UserDTO userFollow) {
        this.userFollow = userFollow;
    }

    public Integer getFollow() {
        return follow;
    }

    public void setFollow(Integer follow) {
        this.follow = follow;
    }
}
