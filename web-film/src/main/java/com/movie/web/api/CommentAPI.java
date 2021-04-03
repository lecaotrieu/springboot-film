package com.movie.web.api;

import com.movie.core.constant.WebConstant;
import com.movie.core.dto.CommentDTO;
import com.movie.core.dto.UserDTO;
import com.movie.core.service.ICommentService;
import com.movie.web.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController(value = "CommentAPIOfWeb")
public class CommentAPI {
    @Autowired
    private ICommentService commentService;

    @PostMapping("/api/film/comment")
    public Long saveComment(@RequestBody CommentDTO commentDTO) throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(SecurityUtils.getUserPrincipal().getId());
        commentDTO.setUser(userDTO);
        return commentService.save(commentDTO);
    }

    // chức năng delete chỉ sử dụng được với 1 id, nhiều id chưa sửa :))
    @DeleteMapping("/api/film/comment")
    public void deleteComment(@RequestBody Long[] ids) {
        if (SecurityUtils.getEmployeeAuthorities().contains(WebConstant.ROLE_ADMIN)) {
            commentService.deleteComment(ids);
        } else if (SecurityUtils.getEmployeeAuthorities().contains(WebConstant.ROLE_POSTER)) {
            CommentDTO commentDTO = commentService.findOneById(ids[0]);
            if (commentDTO.getFilm().getEmployee().getId() == SecurityUtils.getPrincipal().getId()) {
                commentService.deleteComment(ids);
            }
        } else if (SecurityUtils.getUserAuthorities().contains(WebConstant.ROLE_USER)) {
            CommentDTO commentDTO = commentService.findOneById(ids[0]);
            if (commentDTO.getUser().getId() == SecurityUtils.getUserPrincipal().getId()) {
                commentService.deleteComment(ids);
            }
        }
    }

    @GetMapping("/api/admin/comment/content")
    public String loadCommentContent(@RequestParam Long commentId) {
        return commentService.findOneById(commentId).getContent();
    }
}
