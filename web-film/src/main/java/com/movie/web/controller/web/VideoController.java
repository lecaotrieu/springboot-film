package com.movie.web.controller.web;

import com.movie.core.dto.FilmDTO;
import com.movie.core.dto.VideoDTO;
import com.movie.core.service.IFilmService;
import com.movie.core.service.IVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller(value = "VideoControllerOfWeb")
public class VideoController {
    @Autowired
    private IVideoService videoService;

    @RequestMapping(value = "/video/trang-chu", method = RequestMethod.GET)
    public String home(Model model) throws Exception {
        List<VideoDTO> video = videoService.findAll();
        model.addAttribute("video", video);


        return "views/video/home";
    }
}
