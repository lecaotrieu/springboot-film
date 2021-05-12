package com.movie.core.entity;

import javax.persistence.*;

@Entity
@Table(name = "subscribe")
public class SubscribeEntity extends BaseEntity {

    @Column
    private Integer follow;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id_follow")
    private UserEntity userFollow;

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public UserEntity getUserFollow() {
        return userFollow;
    }

    public void setUserFollow(UserEntity userFollow) {
        this.userFollow = userFollow;
    }

    public Integer getFollow() {
        return follow;
    }

    public void setFollow(Integer follow) {
        this.follow = follow;
    }
}
