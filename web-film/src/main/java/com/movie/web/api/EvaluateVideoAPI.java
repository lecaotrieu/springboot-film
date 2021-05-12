package com.movie.web.api;

import com.movie.core.constant.WebConstant;
import com.movie.core.dto.EvaluateVideoDTO;
import com.movie.core.dto.UserDTO;
import com.movie.core.service.IEvaluateVideoService;
import com.movie.core.service.IVideoService;
import com.movie.web.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "EvaluateVideoAPI")
public class EvaluateVideoAPI {
    @Autowired
    private IEvaluateVideoService evaluateVideoService;
    @Autowired
    private IVideoService videoService;

    @PostMapping("/api/video/evaluate")
    public Integer likeVideo(@RequestBody EvaluateVideoDTO evaluateVideoDTO) throws Exception {
        if (SecurityUtils.getUserAuthorities().contains(WebConstant.ROLE_USER)) {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(SecurityUtils.getUserPrincipal().getId());
            evaluateVideoDTO.setUser(userDTO);
            Integer rs = evaluateVideoService.save(evaluateVideoDTO);
            return videoService.setTotalLike(evaluateVideoDTO.getVideo().getId());
        } else {
            throw new Exception();
        }
    }
}
