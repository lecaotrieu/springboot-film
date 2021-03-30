package com.movie.web.controller.web;

import com.movie.core.constant.WebConstant;
import com.movie.core.dto.EvaluateDTO;
import com.movie.core.dto.UserDTO;
import com.movie.core.service.IEvaluateService;
import com.movie.core.service.IUserService;
import com.movie.web.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller(value = "UserControllerOfWen")
public class UserController {
    @Autowired
    private IUserService userService;


    @Autowired
    private IEvaluateService evaluateService;
    @RequestMapping(value = "/trang-ca-nhan", method = RequestMethod.GET)
    public String userInfo(Model model) throws Exception {
        UserDTO userDTO = userService.findOneById(SecurityUtils.getUserPrincipal().getId());

        model.addAttribute("user",userDTO);
        return "views/web/UserProfile";
    }

    @RequestMapping(value = "/chinh-sua-trang-ca-nhan/doi-mat-khau", method = RequestMethod.GET)
    public ModelAndView changePassword() {
        ModelAndView mav = new ModelAndView("web/user/change_password");
        return mav;
    }

    @RequestMapping(value = "/chinh-sua-trang-ca-nhan/doi-avatar", method = RequestMethod.GET)
    public ModelAndView changeAvatar() {
        ModelAndView mav = new ModelAndView("web/user/change_avatar");
        return mav;
    }

    @RequestMapping(value = "/chinh-sua-trang-ca-nhan/cap-nhat-thong-tin", method = RequestMethod.GET)
    public ModelAndView updateInfo() throws Exception {
        UserDTO userDTO = userService.findOneById(SecurityUtils.getUserPrincipal().getId());
        ModelAndView mav = new ModelAndView("web/user/update_info");
        mav.addObject(WebConstant.FORM_ITEM, userDTO);
        return mav;
    }
}
