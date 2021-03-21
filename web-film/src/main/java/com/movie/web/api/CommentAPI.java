package com.movie.web.api;

import com.movie.core.dto.CommentDTO;
import com.movie.core.service.ICommentService;
import com.movie.web.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController(value = "CommentAPIOfWeb")
public class CommentAPI {
    @Autowired
    private ICommentService commentService;
    @PostMapping("/api/film/comment")
    public void saveComment(@RequestBody CommentDTO commentDTO) {
        commentService.save(commentDTO);
    }

    @DeleteMapping("/api/film/comment")
    public void deleteComment(@RequestBody Long[] ids) {
        commentService.deleteComment(ids);
    }

   /* @GetMapping("/ajax/admin/subComment/list")
    public List<CommentDTO> loadSubComment(@RequestBody Long commentId) {
        String employeeUserName = SecurityUtils.getPrincipal().getUsername();
        List<CommentDTO> commentDTOS = commentService.findAllByCommentId(commentId, employeeUserName);
        return commentDTOS;
    }*/
}
