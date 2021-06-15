package com.movie.web.controller.web;

import com.movie.core.constant.WebConstant;
import com.movie.core.dto.CommentVideoDTO;
import com.movie.core.dto.CommentVideoLikeDTO;
import com.movie.core.service.ICommentVideoLikeService;
import com.movie.core.service.ICommentVideoService;
import com.movie.web.command.CommentVideoCommand;
import com.movie.web.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller(value = "CommentVideoControllerOfWeb")
public class CommentVideoController {
    @Autowired
    private ICommentVideoService commentVideoService;

    @Autowired
    private ICommentVideoLikeService commentVideoLikeService;

    @RequestMapping(value = "/ajax/commentVideo/list", method = RequestMethod.GET)
    public String loadCommentVideoList(CommentVideoCommand command, Model model) {
        command.setSortDirection("0");
        command.setLimit(8);
        List<CommentVideoDTO> commentVideoDTOS = commentVideoService.findByProperties(command.getVideoId(), null, "", "", command.getPage(), command.getLimit(), "createdDate", "0");
        command.setListResult(commentVideoDTOS);
        if (SecurityUtils.getUserAuthorities().contains(WebConstant.ROLE_USER)) {
            for (CommentVideoDTO commentVideoDTO : commentVideoDTOS) {
                CommentVideoLikeDTO commentVideoLikeDTO = commentVideoLikeService.findByUserAndComment(SecurityUtils.getUserPrincipal().getId(), commentVideoDTO.getId());
                if (commentVideoLikeDTO != null) {
                    commentVideoDTO.setLike(commentVideoLikeDTO.getStatus());
                }
            }
        }
        command.setTotalItems(commentVideoService.totalComment(command.getVideoId(), null, command.getSearch(), ""));
        command.setTotalPage((int) Math.ceil((double) command.getTotalItems() / command.getLimit()));
        model.addAttribute(WebConstant.LIST_ITEM, command);
        return "views/video/commentVideo/list";
    }


    @RequestMapping(value = "/ajax/commentVideo/new", method = RequestMethod.GET)
    public String loadCommentVideoNew(@RequestParam("id") Long commentId, Model model) {
        CommentVideoDTO commentVideoDTO = commentVideoService.findOneById(commentId);
        if (SecurityUtils.getUserAuthorities().contains(WebConstant.ROLE_USER)) {
            CommentVideoLikeDTO commentVideoLikeDTO = commentVideoLikeService.findByUserAndComment(SecurityUtils.getUserPrincipal().getId(), commentId);
            if (commentVideoLikeDTO != null) {
                commentVideoDTO.setLike(commentVideoLikeDTO.getStatus());
            }
        }
        model.addAttribute("comment", commentVideoDTO);
        return "views/video/commentVideo/newcomment";
    }

    @RequestMapping(value = "/ajax/sub-comment-video/new", method = RequestMethod.GET)
    public String loadSubCommentVideoNew(@RequestParam("id") Long commentId, Model model) {
        CommentVideoDTO commentVideoDTO = commentVideoService.findOneById(commentId);
        if (SecurityUtils.getUserAuthorities().contains(WebConstant.ROLE_USER)) {
            CommentVideoLikeDTO commentVideoLikeDTO = commentVideoLikeService.findByUserAndComment(SecurityUtils.getUserPrincipal().getId(), commentId);
            if (commentVideoLikeDTO != null) {
                commentVideoDTO.setLike(commentVideoLikeDTO.getStatus());
            }
        }
        model.addAttribute("comment", commentVideoDTO);
        return "views/video/commentVideo/newsubcomment";
    }


    @RequestMapping(value = "/ajax/subCommentVideo/list", method = RequestMethod.GET)
    public String loadSubCommentVideoList(CommentVideoCommand command, Model model) {
        command.setLimit(10);
        List<CommentVideoDTO> commentVideoDTOS = commentVideoService.findByProperties(command.getCommentId(), command.getVideoId(), command.getPage(), command.getLimit(), "createdDate", "0");
        command.setListResult(commentVideoDTOS);
        if (SecurityUtils.getUserAuthorities().contains(WebConstant.ROLE_USER)) {
            for (CommentVideoDTO commentVideoDTO : commentVideoDTOS) {
                CommentVideoLikeDTO commentVideoLikeDTO = commentVideoLikeService.findByUserAndComment(SecurityUtils.getUserPrincipal().getId(), commentVideoDTO.getId());
                if (commentVideoLikeDTO != null) {
                    commentVideoDTO.setLike(commentVideoLikeDTO.getStatus());
                }
            }
        }
        int totalItem = commentVideoService.totalComment(command.getCommentId(), command.getVideoId());
        int n = totalItem - command.getPage() * command.getLimit();
        if (n > 10) {
            command.setNextCountItem(10);
        } else {
            command.setNextCountItem(n);
        }
        model.addAttribute(WebConstant.LIST_ITEM, command);
        return "views/video/commentVideo/subcm-list";
    }
}
