package com.movie.web.api;

import com.movie.core.dto.CommentDTO;
import com.movie.core.dto.CommentLikeDTO;
import com.movie.core.dto.UserDTO;
import com.movie.core.service.ICommentLikeService;
import com.movie.core.service.ICommentService;
import com.movie.web.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "CommentLike")
public class CommentLikeAPI {
    @Autowired
    private ICommentLikeService commentLikeService;

    @Autowired
    private ICommentService commentService;

    @PostMapping("/api/film/comment/like")
    public Integer saveCommentLike(@RequestBody CommentLikeDTO dto) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(SecurityUtils.getUserPrincipal().getId());
        dto.setUser(userDTO);
        commentLikeService.save(dto);
        CommentDTO commentDTO = commentService.findOneById(dto.getComment().getId());
        return commentDTO.getTotalLike();
    }
}
