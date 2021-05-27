package com.movie.web.controller.admin;

import com.movie.core.constant.WebConstant;
import com.movie.core.dto.CategoryDTO;
import com.movie.core.dto.FilmTypeDTO;
import com.movie.core.service.*;
import com.movie.web.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller(value = "HomeControllerOfAdmin")
public class HomeController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IEmployeeService employeeService;
    @Autowired
    private IVideoService videoService;
    @Autowired
    private IFilmService filmService;
    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IFilmTypeService filmTypeService;

    @RequestMapping(value = {"/admin/home-page", "/admin/trang-chu"}, method = RequestMethod.GET)
    public String homePage(Model model) {
//        String userInfo = SecurityUtils.getPrincipal().getUsername();
//        model.addAttribute("userInfo", userInfo);
        String userName = "";
        if (SecurityUtils.getEmployeeAuthorities().contains(WebConstant.ROLE_ADMIN) || SecurityUtils.getEmployeeAuthorities().contains(WebConstant.ROLE_MANAGER)) {
            userName = "ADMIN";
        } else if (SecurityUtils.getEmployeeAuthorities().contains(WebConstant.ROLE_POSTER)) {
            userName = SecurityUtils.getPrincipal().getUsername();
        }
        if (SecurityUtils.getEmployeeAuthorities().contains(WebConstant.ROLE_ADMIN) || SecurityUtils.getEmployeeAuthorities().contains(WebConstant.ROLE_POSTER)) {
            int totalFilm = filmService.getTotalItem(userName, "", "", "", "", "");
            model.addAttribute("filmTotal", totalFilm);
        }

        if (userName == "ADMIN") {
            int totalEmployee = employeeService.getTotalItem();
            model.addAttribute("employeeTotal", totalEmployee);
        }
        if (SecurityUtils.getEmployeeAuthorities().contains(WebConstant.ROLE_ADMIN) || SecurityUtils.getEmployeeAuthorities().contains(WebConstant.ROLE_MANAGER)) {
            int totalUser = userService.getTotalItem("");
            model.addAttribute("userTotal", totalUser);
            List<CategoryDTO> categoryDTOS = categoryService.findAll();
            List<FilmTypeDTO> filmTypeDTOS = filmTypeService.findAll();
            model.addAttribute("categories", categoryDTOS);
            model.addAttribute("filmTypes", filmTypeDTOS);
        }
        if (SecurityUtils.getEmployeeAuthorities().contains(WebConstant.ROLE_ADMIN) || SecurityUtils.getEmployeeAuthorities().contains(WebConstant.ROLE_MANAGER)) {
            int totalVideo = videoService.getTotalItem("", "");
            model.addAttribute("videoTotal", totalVideo);
        }
        return "views/admin/home";
    }
}
