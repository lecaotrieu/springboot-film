package com.movie.web.api;

import com.movie.core.dto.CommentVideoDTO;
import com.movie.core.dto.UserDTO;
import com.movie.core.service.ICommentVideoService;
import com.movie.web.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class CommentVideoAPI {
    @Autowired
    private ICommentVideoService commentVideoService;

    @PostMapping("/api/video/comment")
    public Long saveComment(@RequestBody CommentVideoDTO commentVideoDTO) throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(SecurityUtils.getUserPrincipal().getId());
        commentVideoDTO.setUser(userDTO);
        return commentVideoService.save(commentVideoDTO);
    }

    @DeleteMapping("/api/video/comment")
    public void deleteCommentVideo(@RequestBody Long[] ids) {
        commentVideoService.deleteCommentVideo(ids);
    }

    @GetMapping("/api/admin/comment-video/content")
    public String loadCommentVideoContent(@RequestParam Long commentId) {
        return commentVideoService.findOneById(commentId).getContent();
    }

}
