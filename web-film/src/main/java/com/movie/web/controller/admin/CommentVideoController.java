package com.movie.web.controller.admin;

import com.movie.core.constant.WebConstant;
import com.movie.core.dto.CommentVideoDTO;
import com.movie.core.service.ICommentVideoService;
import com.movie.core.service.IVideoService;
import com.movie.core.utils.WebCommonUtil;
import com.movie.web.command.CommentVideoCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller(value = "CommentVideoControllerOfAdmin")
public class CommentVideoController {

    ResourceBundle bundle = ResourceBundle.getBundle("i18n/message_vn");

    @Autowired
    private ICommentVideoService commentVideoService;

    @Autowired
    private IVideoService videoService;

    @RequestMapping(value = "/admin/comment-video/list", method = RequestMethod.GET)
    public String showCommentVideoList(@ModelAttribute CommentVideoCommand command, Model model) {
        if (command.getSortExpression() == null) {
            command.setSortExpression("createdDate");
        }
        if (command.getMessage() != null) {
            WebCommonUtil.addRedirectMessage(model, getMapMessage(), command.getMessage());
        }
        List<CommentVideoDTO> commentVideoDTOS = new ArrayList<>();
        commentVideoDTOS = commentVideoService.findByProperties(command.getVideoId(), command.getCommentId(), command.getSearch(),command.getUserName(), command.getPage(), command.getLimit(), command.getSortExpression(), command.getSortDirection());
        command.setTotalItems(commentVideoService.totalComment(command.getVideoId(), command.getCommentId(), command.getSearch(), command.getUserName()));
        command.setListResult(commentVideoDTOS);
        command.setTotalPage((int) Math.ceil((double) command.getTotalItems() / command.getLimit()));
        model.addAttribute(WebConstant.LIST_ITEM, command);
        if (command.getVideoId() != null) {
            model.addAttribute("video", videoService.findOneById(command.getVideoId()));
        }
        return "views/admin/commentVideo/list";
    }


    @RequestMapping(value = "/ajax/admin/subComment-video/list", method = RequestMethod.GET)
    public String loadSubCommentList(Model model, @RequestParam("commentId") Long commentId) {
        List<CommentVideoDTO> commentVideoDTOS = new ArrayList<>();
        commentVideoDTOS = commentVideoService.findAllByCommentId(commentId);
        model.addAttribute("commentVideos", commentVideoDTOS);
        model.addAttribute("commentVideoId", commentId);
        return "views/admin/commentVideo/subCommentList";
    }


    private Map<String, String> getMapMessage() {
        Map<String, String> mapValue = new HashMap<String, String>();
        mapValue.put(WebConstant.REDIRECT_ERROR, bundle.getString("label.message.error"));
        mapValue.put(WebConstant.REDIRECT_INSERT, bundle.getString("label.comment.message.add.success"));
        mapValue.put(WebConstant.REDIRECT_DELETE, bundle.getString("label.comment.message.delete.success"));
        mapValue.put(WebConstant.REDIRECT_UPDATE, bundle.getString("label.comment.message.update.success"));
        return mapValue;
    }
}
