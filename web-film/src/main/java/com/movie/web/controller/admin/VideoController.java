package com.movie.web.controller.admin;

import com.movie.core.constant.WebConstant;
import com.movie.core.dto.VideoDTO;
import com.movie.core.service.IVideoService;
import com.movie.core.utils.FormUtil;
import com.movie.core.utils.RequestUtil;
import com.movie.core.utils.WebCommonUtil;
import com.movie.web.command.VideoCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

@Controller(value = "VideoControllerOfAdmin")
public class VideoController {
    ResourceBundle bundle = ResourceBundle.getBundle("i18n/message_vn");

    @Autowired
    private IVideoService videoService;

    @RequestMapping(value = {"/admin/video/list"}, method = RequestMethod.GET)
    public String showVideoList(Model model, HttpServletRequest request) {
        VideoCommand command = FormUtil.populate(VideoCommand.class, request);
        RequestUtil.initSearchBeanManual(command);
        executeSearchVideo(command);
        if (command.getMessage() != null) {
            WebCommonUtil.addRedirectMessage(model, getMapMessage(), command.getMessage());
        }
        model.addAttribute(WebConstant.LIST_ITEM, command);
        return "views/admin/video/list";
    }


    @RequestMapping(value = {"/admin/video/show"}, method = RequestMethod.GET)
    public String showVideo(Model model, @RequestParam(value = "message", required = false) String message, @RequestParam("id") Long id) {
        if (message != null) {
            WebCommonUtil.addRedirectMessage(model, getMapMessage(), message);
        }
        VideoDTO videoDTO = videoService.findOneById(id);
        model.addAttribute(WebConstant.FORM_ITEM, videoDTO);
        return "views/admin/video/show";
    }

    private Map<String, String> getMapMessage() {
        Map<String, String> mapValue = new HashMap<String, String>();
        mapValue.put(WebConstant.REDIRECT_ERROR, bundle.getString("label.message.error"));
        mapValue.put(WebConstant.REDIRECT_INSERT, bundle.getString("label.video.message.add.success"));
        mapValue.put(WebConstant.REDIRECT_DELETE, bundle.getString("label.video.message.delete.success"));
        mapValue.put(WebConstant.REDIRECT_UPDATE, bundle.getString("label.video.message.update.success"));
        return mapValue;
    }

    private void executeSearchVideo(VideoCommand command) {
        List<VideoDTO> videoDTOS = videoService.findByProperties(command.getSearch(), command.getUserName(), command.getStatus(), command.getPage(), command.getLimit(), command.getSortExpression(), command.getSortDirection());
        command.setListResult(videoDTOS);
        command.setTotalItems(videoService.getTotalItem(command.getSearch(), command.getUserName(), command.getStatus()));
        command.setTotalPage((int) Math.ceil((double) command.getTotalItems() / command.getLimit()));
    }
}
