package com.movie.web.api;

import com.movie.core.constant.WebConstant;
import com.movie.core.dto.SubscribeDTO;
import com.movie.core.dto.UserDTO;
import com.movie.core.service.ISubscribeService;
import com.movie.core.service.IUserService;
import com.movie.web.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "SubscribeAPI")
public class SubscribeAPI {
    @Autowired
    private ISubscribeService subscribeService;
    @Autowired
    private IUserService userService;

    @PostMapping("/api/user/subscribe")
    public Integer likeVideo(@RequestBody SubscribeDTO subscribeDTO) throws Exception {
        if (SecurityUtils.getUserAuthorities().contains(WebConstant.ROLE_USER)) {
            if (SecurityUtils.getUserPrincipal().getId() != subscribeDTO.getUserFollow().getId()) {
                UserDTO userDTO = new UserDTO();
                userDTO.setId(SecurityUtils.getUserPrincipal().getId());
                subscribeDTO.setUser(userDTO);
                Integer rs = subscribeService.save(subscribeDTO);
                return userService.setTotalFollow(subscribeDTO.getUserFollow().getId());
            } else {
                throw new Exception();
            }
        } else {
            throw new Exception();
        }
    }
}
