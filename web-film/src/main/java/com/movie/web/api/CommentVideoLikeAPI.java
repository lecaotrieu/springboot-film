package com.movie.web.api;

import com.movie.core.dto.CommentVideoDTO;
import com.movie.core.dto.CommentVideoLikeDTO;
import com.movie.core.dto.UserDTO;
import com.movie.core.service.ICommentVideoLikeService;
import com.movie.core.service.ICommentVideoService;
import com.movie.web.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "CommentVideoLikeAPI")
public class CommentVideoLikeAPI {
    @Autowired
    private ICommentVideoLikeService commentVideoLikeService;

    @Autowired
    private ICommentVideoService commentVideoService;

    @PostMapping("/api/video/comment/like")
    public Integer saveCommentVideoLike(@RequestBody CommentVideoLikeDTO dto) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(SecurityUtils.getUserPrincipal().getId());
        dto.setUser(userDTO);
        commentVideoLikeService.save(dto);
        CommentVideoDTO commentVideoDTO = commentVideoService.findOneById(dto.getCommentVideo().getId());
        return commentVideoDTO.getTotalLike();
    }
}
