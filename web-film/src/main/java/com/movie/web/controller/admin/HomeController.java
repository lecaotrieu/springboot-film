package com.movie.web.controller.admin;

import com.movie.core.service.IDriveService;
import com.movie.web.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller(value = "HomeControllerOfAdmin")
public class HomeController {
    @Autowired
    private IDriveService driveService;

    @RequestMapping(value = {"/admin/home-page", "a/trang-chu", "/admin/trang-chu"}, method = RequestMethod.GET)
    public String homePage(Model model) {
        String userInfo = SecurityUtils.getPrincipal().getUsername();
        model.addAttribute("userInfo", userInfo);
        return "views/admin/home";
    }
}
