package com.movie.web.controller.web;

import com.movie.core.constant.WebConstant;
import com.movie.core.dto.CommentDTO;
import com.movie.core.dto.CommentLikeDTO;
import com.movie.core.service.ICommentLikeService;
import com.movie.core.service.ICommentService;
import com.movie.core.utils.RequestUtil;
import com.movie.web.command.CommentCommand;
import com.movie.web.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class CommentController {
    @Autowired
    private ICommentService commentService;

    @Autowired
    private ICommentLikeService commentLikeService;

    @RequestMapping(value = "/ajax/comment/list", method = RequestMethod.GET)
    public String loadCommentList(CommentCommand command, Model model) {
        command.setSortDirection("0");
        command.setLimit(8);
        List<CommentDTO> commentDTOS = commentService.findByProperties(command.getFilmId(), null, "", "", command.getPage(), command.getLimit(), "createdDate", "0");
        command.setListResult(commentDTOS);
        if (SecurityUtils.getUserAuthorities().contains(WebConstant.ROLE_USER)) {
            for (CommentDTO commentDTO : commentDTOS) {
                CommentLikeDTO commentLikeDTO = commentLikeService.findByUserAndComment(SecurityUtils.getUserPrincipal().getId(), commentDTO.getId());
                if (commentLikeDTO != null) {
                    commentDTO.setLike(commentLikeDTO.getStatus());
                }
            }
        }
        command.setTotalItems(commentService.totalComment(command.getFilmId(), null, command.getSearch(), ""));
        command.setTotalPage((int) Math.ceil((double) command.getTotalItems() / command.getLimit()));
        model.addAttribute(WebConstant.LIST_ITEM, command);
        return "views/web/comment/list";
    }


    @RequestMapping(value = "/ajax/comment/new", method = RequestMethod.GET)
    public String loadCommentNew(@RequestParam("id") Long commentId, Model model) {
        CommentDTO commentDTO = commentService.findOneById(commentId);
        if (SecurityUtils.getUserAuthorities().contains(WebConstant.ROLE_USER)) {
            CommentLikeDTO commentLikeDTO = commentLikeService.findByUserAndComment(SecurityUtils.getUserPrincipal().getId(), commentId);
            if (commentLikeDTO != null) {
                commentDTO.setLike(commentLikeDTO.getStatus());
            }
        }
        model.addAttribute("comment", commentDTO);
        return "views/web/comment/newcomment";
    }

    @RequestMapping(value = "/ajax/sub-comment/new", method = RequestMethod.GET)
    public String loadSubCommentNew(@RequestParam("id") Long commentId, Model model) {
        CommentDTO commentDTO = commentService.findOneById(commentId);
        if (SecurityUtils.getUserAuthorities().contains(WebConstant.ROLE_USER)) {
            CommentLikeDTO commentLikeDTO = commentLikeService.findByUserAndComment(SecurityUtils.getUserPrincipal().getId(), commentId);
            if (commentLikeDTO != null) {
                commentDTO.setLike(commentLikeDTO.getStatus());
            }
        }
        model.addAttribute("comment", commentDTO);
        return "views/web/comment/newsubcomment";
    }


    @RequestMapping(value = "/ajax/comment/edit", method = RequestMethod.GET)
    public ModelAndView loadCommentEdit(@RequestParam("filmId") Long filmId) {
        ModelAndView mav = new ModelAndView("web/comment/edit");
        CommentCommand command = new CommentCommand();
        command.setFilmId(filmId);
        command.setTotalItems(commentService.totalComment(filmId));
        mav.addObject(WebConstant.FORM_ITEM, command);
        return mav;
    }

    @RequestMapping(value = "/ajax/subComment/list", method = RequestMethod.GET)
    public String loadSubCommentList(CommentCommand command, Model model) {
        command.setLimit(10);
        List<CommentDTO> commentDTOS = commentService.findByProperties(command.getCommentId(), command.getFilmId(), command.getPage(), command.getLimit(), "createdDate", "0");
        command.setListResult(commentDTOS);
        if (SecurityUtils.getUserAuthorities().contains(WebConstant.ROLE_USER)) {
            for (CommentDTO commentDTO : commentDTOS) {
                CommentLikeDTO commentLikeDTO = commentLikeService.findByUserAndComment(SecurityUtils.getUserPrincipal().getId(), commentDTO.getId());
                if (commentLikeDTO != null) {
                    commentDTO.setLike(commentLikeDTO.getStatus());
                }
            }
        }
        int totalItem = commentService.totalComment(command.getCommentId(), command.getFilmId());
        int n = totalItem - command.getPage() * command.getLimit();
        if (n > 10) {
            command.setNextCountItem(10);
        } else {
            command.setNextCountItem(n);
        }
        model.addAttribute(WebConstant.LIST_ITEM, command);
        return "views/web/comment/subcm-list";
    }
}
