package com.movie.web.controller.admin;

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
    private IFilmTypeService  filmTypeService;

    @RequestMapping(value = {"/admin/home-page", "/admin/trang-chu"}, method = RequestMethod.GET)
    public String homePage(Model model) {
//        String userInfo = SecurityUtils.getPrincipal().getUsername();
//        model.addAttribute("userInfo", userInfo);
        int totalUser = userService.getTotalItem("");
        int totalEmployee = employeeService.getTotalItem();
        int totalVideo = videoService.getTotalItem("", "");
        int totalFilm = filmService.getTotalItem();
        List<CategoryDTO> categoryDTOS = categoryService.findAll();
        List<FilmTypeDTO> filmTypeDTOS = filmTypeService.findAll();
        model.addAttribute("userTotal", totalUser);
        model.addAttribute("employeeTotal", totalEmployee);
        model.addAttribute("videoTotal", totalVideo);
        model.addAttribute("filmTotal", totalFilm);
        model.addAttribute("categories", categoryDTOS);
        model.addAttribute("filmTypes", filmTypeDTOS);
        return "views/admin/home";
    }
}
