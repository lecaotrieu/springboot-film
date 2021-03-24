package com.movie.web.api;

import com.movie.core.service.ICommentVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class CommentVideoAPI {
    @Autowired
    private ICommentVideoService commentVideoService;

    @DeleteMapping("/api/video/comment")
    public void deleteCommentVideo(@RequestBody Long[] ids) {
        commentVideoService.deleteCommentVideo(ids);
    }

    @GetMapping("/api/admin/comment-video/content")
    public String loadCommentVideoContent(@RequestParam Long commentId) {
        return commentVideoService.findOneById(commentId).getContent();
    }

}
