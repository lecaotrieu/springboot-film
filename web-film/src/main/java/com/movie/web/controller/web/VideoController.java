package com.movie.web.controller.web;

import com.movie.core.constant.CoreConstant;
import com.movie.core.constant.WebConstant;
import com.movie.core.dto.UserDTO;
import com.movie.core.dto.VideoDTO;
import com.movie.core.service.IUserService;
import com.movie.core.service.IVideoService;
import com.movie.web.command.VideoCommand;
import com.movie.web.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Random;


@Controller(value = "VideoControllerOfWeb")
public class VideoController {
    @Autowired
    private IVideoService videoService;

    @Autowired
    private IUserService userService;

    @RequestMapping(value = "/video/trang-chu", method = RequestMethod.GET)
    public String home(Model model) throws Exception {
        List<VideoDTO> topVideos = videoService.findByProperties("", CoreConstant.ACTIVE_STATUS, 1, 8, "view", "0");
        List<VideoDTO> videos = videoService.findAllToViewInHomePage(CoreConstant.ACTIVE_STATUS, 1, 20);
        model.addAttribute("videos", videos);
        model.addAttribute("topVideos", topVideos);
        return "views/video/home";
    }

    @RequestMapping(value = "/video/thinh-hanh", method = RequestMethod.GET)
    public String trending(Model model) throws Exception {
        List<VideoDTO> topVideos = videoService.findByProperties("", CoreConstant.ACTIVE_STATUS, 1, 20, "view", "0");
        model.addAttribute("topVideos", topVideos);
        return "views/video/video-trend";
    }

    @RequestMapping(value = "/trang-ca-nhan/quan-ly-video", method = RequestMethod.GET)
    public String profile(Model model) throws Exception {
        List<VideoDTO> videoDTOS = videoService.findByProperties(SecurityUtils.getUserPrincipal().getId());
        model.addAttribute("videos", videoDTOS);
        UserDTO userDTO = userService.findOneById(SecurityUtils.getUserPrincipal().getId());
        model.addAttribute("user", userDTO);
        return "views/video/MyVideo";
    }

    @RequestMapping(value = "/video/xem-video/{id}", method = RequestMethod.GET)
    public String showVideo(Model model, @PathVariable("id") Long id) throws Exception {
        Integer limit = 10;
        VideoDTO videoDTO = videoService.findOneByIdAndStatus(id, CoreConstant.ACTIVE_STATUS);
        model.addAttribute("video", videoDTO);
        // random page
        Integer totalItem = videoService.getTotalItem("", CoreConstant.ACTIVE_STATUS);
        Integer totalPage = (int) Math.ceil((double) totalItem / limit);
        Random random = new Random();
        Integer page = random.nextInt(totalPage) + 1;
        List<VideoDTO> videoNominates = videoService.findByProperties("", CoreConstant.ACTIVE_STATUS, page, limit, null, null);
        model.addAttribute("videoNominates", videoNominates);
        return "views/video/videoSingle";
    }

    @RequestMapping(value = "/danh-sach-video-{userId}", method = RequestMethod.GET)
    public String userVideo(Model model,@PathVariable(value = "userId", required = false) Long userId) throws Exception {
        List<VideoDTO> videoDTOS = videoService.findByProperties(userId);
        model.addAttribute("videos", videoDTOS);
        UserDTO userDTO = userService.findOneById(userId);
        model.addAttribute("user", userDTO);
        return "views/video/UserVideo";
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
