package com.movie.web.controller.admin;

import com.movie.core.constant.WebConstant;
import com.movie.core.dto.CommentDTO;
import com.movie.core.dto.EmployeeDTO;
import com.movie.core.service.ICommentLikeService;
import com.movie.core.service.ICommentService;
import com.movie.core.service.IEmployeeService;
import com.movie.core.service.IFilmService;
import com.movie.core.utils.WebCommonUtil;
import com.movie.web.command.CommentCommand;
import com.movie.web.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller(value = "CommentControllerOfAdmin")
public class CommentController {

    ResourceBundle bundle = ResourceBundle.getBundle("i18n/message_vn");

    @Autowired
    private ICommentService commentService;

    @Autowired
    private ICommentLikeService commentLikeService;

    @RequestMapping(value = "/ajax/admin/comment/list", method = RequestMethod.GET)
    public ModelAndView loadCommentList(@ModelAttribute CommentCommand command) {
        ModelAndView mav = new ModelAndView("views/admin/comment/list");
        List<CommentDTO> commentDTOS = commentService.findByProperties(command.getFilmId(), command.getPage(), command.getLimit(), "createdDate", command.getSortDirection());
        command.setListResult(commentDTOS);
        for (CommentDTO commentDTO : commentDTOS) {
            commentDTO.setTotalLike(commentLikeService.totalCommentLike(commentDTO.getId()));
        }
        command.setTotalItems(commentService.totalComment(command.getFilmId()));
        mav.addObject(WebConstant.LIST_ITEM, command);
        command.setTotalPage((int) Math.ceil((double) command.getTotalItems() / command.getLimit()));
        return mav;
    }

    @Autowired
    private IFilmService filmService;

    @RequestMapping(value = "/admin/comment/list", method = RequestMethod.GET)
    public String showCommentList(@ModelAttribute CommentCommand command, Model model) {
        if (command.getSortExpression() == null) {
            command.setSortExpression("createdDate");
        }
        if (command.getMessage() != null) {
            WebCommonUtil.addRedirectMessage(model, getMapMessage(), command.getMessage());
        }
        List<CommentDTO> commentDTOS = new ArrayList<>();
        if (SecurityUtils.getEmployeeAuthorities().contains(WebConstant.ROLE_ADMIN)) {
            commentDTOS = commentService.findByProperties(command.getFilmId(), command.getCommentId(), command.getSearch(), command.getUserName(), command.getPage(), command.getLimit(), command.getSortExpression(), command.getSortDirection());
            command.setTotalItems(commentService.totalComment(command.getFilmId(), command.getCommentId(), command.getSearch(), command.getUserName()));
        } else if (SecurityUtils.getEmployeeAuthorities().contains(WebConstant.ROLE_POSTER)) {
            String employeeUserName = SecurityUtils.getPrincipal().getUsername();
            commentDTOS = commentService.findByProperties(command.getFilmId(), command.getCommentId(), command.getSearch(), command.getUserName(), employeeUserName, command.getPage(), command.getLimit(), command.getSortExpression(), command.getSortDirection());
            command.setTotalItems(commentService.totalComment(command.getFilmId(), command.getCommentId(), command.getSearch(), command.getUserName(), employeeUserName));
        }
        command.setListResult(commentDTOS);
        command.setTotalPage((int) Math.ceil((double) command.getTotalItems() / command.getLimit()));
        model.addAttribute(WebConstant.LIST_ITEM, command);
        if (command.getFilmId()!=null){
            model.addAttribute("film", filmService.findOneById(command.getFilmId()));
        }
        return "views/admin/comment/list";
    }

    @RequestMapping(value = "/ajax/admin/comment/edit", method = RequestMethod.GET)
    public ModelAndView loadCommentEdit(@RequestParam("filmId") Long filmId) {
        ModelAndView mav = new ModelAndView("views/admin/comment/edit");
        CommentCommand command = new CommentCommand();
        command.setFilmId(filmId);
        command.setTotalItems(commentService.totalComment(filmId));
        mav.addObject(WebConstant.FORM_ITEM, command);
        return mav;
    }

   /* @RequestMapping(value = "/ajax/admin/subComment/list", method = RequestMethod.GET)
    public ModelAndView loadSubCommentList(@ModelAttribute CommentCommand command) {
        ModelAndView mav = new ModelAndView("views/admin/comment/subCommentList");
        List<CommentDTO> commentDTOS = commentService.findByProperties(command.getCommentId(), command.getFilmId(), command.getPage(), command.getLimit(), "createdDate", command.getSortDirection());
        command.setListResult(commentDTOS);
        for (CommentDTO commentDTO : commentDTOS) {
            commentDTO.setTotalLike(commentLikeService.totalCommentLike(commentDTO.getId()));
        }
        command.setTotalItems(commentService.totalComment(command.getFilmId()));
        int totalItem = commentService.totalComment(command.getCommentId(), command.getFilmId());
        int n = totalItem - command.getPage() * command.getLimit();
        if (n > 10) {
            command.setNextCountItem(10);
        } else {
            command.setNextCountItem(n);
        }
        mav.addObject(WebConstant.LIST_ITEM, command);
        return mav;
    }*/

    @RequestMapping(value = "/ajax/admin/subComment/list", method = RequestMethod.GET)
    public String loadSubCommentList(Model model, @RequestParam("commentId") Long commentId) {
        List<CommentDTO> commentDTOS = new ArrayList<>();
        if (SecurityUtils.getEmployeeAuthorities().contains(WebConstant.ROLE_ADMIN)) {
            commentDTOS = commentService.findAllByCommentId(commentId);
        } else if (SecurityUtils.getEmployeeAuthorities().contains(WebConstant.ROLE_POSTER)) {
            String employeeUserName = SecurityUtils.getPrincipal().getUsername();
            commentDTOS = commentService.findAllByCommentId(commentId, employeeUserName);
        }
        model.addAttribute("comments", commentDTOS);
        model.addAttribute("commentId", commentId);
        return "views/admin/comment/subCommentList";
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
