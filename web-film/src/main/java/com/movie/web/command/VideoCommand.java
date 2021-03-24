package com.movie.web.command;

import com.movie.core.command.AbstractCommand;
import com.movie.core.dto.VideoDTO;

public class VideoCommand extends AbstractCommand<VideoDTO> {
    public VideoCommand() {
        this.pojo = new VideoDTO();
    }

    private String userName;
    private Integer status;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
