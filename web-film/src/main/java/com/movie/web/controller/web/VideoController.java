package com.movie.web.controller.web;

import com.movie.core.constant.CoreConstant;
import com.movie.core.constant.WebConstant;
import com.movie.core.dto.VideoDTO;
import com.movie.core.service.IVideoService;
import com.movie.web.command.VideoCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import java.util.List;

@Controller(value = "VideoControllerOfWeb")
public class VideoController {
    @Autowired
    private IVideoService videoService;

    @RequestMapping(value = "/video/trang-chu", method = RequestMethod.GET)
    public String home(Model model) throws Exception {
        List<VideoDTO> topVideos = videoService.findByProperties("", CoreConstant.ACTIVE_STATUS, 1, 8, "view", "0");
        List<VideoDTO> videos = videoService.findAllToViewInHomePage(CoreConstant.ACTIVE_STATUS, 1, 20);
        model.addAttribute("videos", videos);
        model.addAttribute("topVideos", topVideos);
        return "views/video/home";
    }

    @RequestMapping(value = "/ajax/video/get-list", method = RequestMethod.GET)
    public String getListVideo(Model model, @RequestParam("page") Integer page, @RequestParam("limit") Integer limit) throws Exception {
        List<VideoDTO> videos = videoService.findAllToViewInHomePage(CoreConstant.ACTIVE_STATUS, page, limit);
        model.addAttribute("videos", videos);
        model.addAttribute("page", page);
        model.addAttribute("limit", limit);
        return "views/video/ajaxhtml/loadListVideo";
    }

    @RequestMapping(value = "/video/tim-kiem", method = RequestMethod.GET)
    public String videoSearch(Model model, @RequestParam(value = "page", required = false) Integer page,
                              @RequestParam(value = "limit", required = false) Integer limit,
                              @RequestParam(value = "sort", required = false) String sort,
                              @RequestParam(value = "d", required = false) String sortDsc,
                              @RequestParam(value = "search", required = false) String search) throws Exception {
        VideoCommand command = addValueToCommand(page, limit, sort, sortDsc, search);
        if (limit != null) command.setLimit(limit);
        command.setSortExpression("view");
        command.setSortDirection("0");
        List<VideoDTO> videoDTOS = videoService.findByProperties(command.getSearch(), CoreConstant.ACTIVE_STATUS, command.getPage(), command.getLimit(), command.getSortExpression(), command.getSortDirection());
        command.setListResult(videoDTOS);
        command.setTotalItems(videoService.getTotalItem(command.getSearch(), CoreConstant.ACTIVE_STATUS));
        command.setTotalPage((int) Math.ceil((double) command.getTotalItems() / command.getLimit()));
        model.addAttribute(WebConstant.LIST_ITEM, command);
        return "views/video/videoList";
    }

    private VideoCommand addValueToCommand(Integer page, Integer limit, String sort, String sortDsc, String search) {
        VideoCommand command = new VideoCommand();
        if (page != null) {
            command.setPage(page);
        }
        if (limit != null) {
            command.setLimit(limit);
        }
        if (sort != null) {
            command.setSortExpression(sort);
        }
        if (sortDsc != null) {
            command.setSortDirection(sortDsc);
        }
        if (search != null) {
            command.setSearch(search);
        }
        return command;
    }
}
