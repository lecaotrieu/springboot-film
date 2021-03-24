package com.movie.web.command;

import com.movie.core.command.AbstractCommand;
import com.movie.core.dto.CommentVideoDTO;

public class CommentVideoCommand extends AbstractCommand<CommentVideoDTO> {
    public CommentVideoCommand() {
        this.pojo = new CommentVideoDTO();
    }

    private Long videoId;
    private Long commentId;
    private Integer nextCountItem;
    private String userName = "";

    public Long getVideoId() {
        return videoId;
    }

    public void setVideoId(Long videoId) {
        this.videoId = videoId;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Integer getNextCountItem() {
        return nextCountItem;
    }

    public void setNextCountItem(Integer nextCountItem) {
        this.nextCountItem = nextCountItem;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
