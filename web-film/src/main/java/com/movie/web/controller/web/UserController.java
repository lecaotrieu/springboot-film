package com.movie.web.controller.web;

import com.movie.core.constant.WebConstant;
import com.movie.core.dto.EvaluateDTO;
import com.movie.core.dto.UserDTO;
import com.movie.core.service.IEvaluateService;
import com.movie.core.service.IUserService;
import com.movie.core.utils.WebCommonUtil;
import com.movie.web.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

@Controller(value = "UserControllerOfWen")
public class UserController {
    @Autowired
    private IUserService userService;

    ResourceBundle bundle = ResourceBundle.getBundle("i18n/message_vn");

    @Autowired
    private IEvaluateService evaluateService;

    @RequestMapping(value = "/trang-ca-nhan", method = RequestMethod.GET)
    public String userInfo(Model model, @RequestParam(value = "message", required = false) String message) throws Exception {
        UserDTO userDTO = userService.findOneById(SecurityUtils.getUserPrincipal().getId());
        if (message != null) {
            WebCommonUtil.addRedirectMessage(model, getMapMessage(), message);
        }
        model.addAttribute("user", userDTO);
        return "views/web/UserProfile";
    }

    private Map<String, String> getMapMessage() {
        Map<String, String> mapValue = new HashMap<String, String>();
        mapValue.put(WebConstant.REDIRECT_ERROR, bundle.getString("label.message.error"));
        mapValue.put(WebConstant.REDIRECT_INSERT, bundle.getString("label.user.message.add.success"));
        mapValue.put(WebConstant.REDIRECT_DELETE, bundle.getString("label.user.message.delete.success"));
        mapValue.put(WebConstant.REDIRECT_UPDATE, bundle.getString("label.user.message.update.success"));
        return mapValue;
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

    @RequestMapping(value = "/trang-ca-nhan/edit", method = RequestMethod.POST)
    public String saveProfile(UserDTO userDTO) {
        userDTO.setId(SecurityUtils.getUserPrincipal().getId());
        userDTO.setUserName(SecurityUtils.getUserPrincipal().getUsername());
        try {
            userService.save(userDTO);
            return "redirect:/trang-ca-nhan?message=redirect_update_password";
        } catch (Exception e) {
            return "redirect:/trang-ca-nhan?message=redirect_error";
        }
    }

    @RequestMapping(value = "/trang-ca-nhan/doi-mat-khau", method = RequestMethod.POST)
    public String changePassword(UserDTO userDTO) throws Exception {
        if (SecurityUtils.getUserAuthorities().contains(WebConstant.ROLE_USER)) {
            userDTO.setId(SecurityUtils.getUserPrincipal().getId());
            userService.updatePassword(userDTO);
            return "redirect:/trang-ca-nhan?message=redirect_update";
        }
        return "redirect:/trang-ca-nhan?message=redirect_error";
    }

    @RequestMapping(value = "/dang-ki", method = RequestMethod.POST)
    public String registerUser(UserDTO userDTO) {

        try {
            userService.save(userDTO);
            return "redirect:/ajax-register-success";
        } catch (Exception e) {
            return "redirect:/ajax-register-failure";
        }
    }

}
