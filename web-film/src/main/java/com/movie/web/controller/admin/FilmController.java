package com.movie.web.controller.admin;

import com.movie.core.constant.WebConstant;
import com.movie.core.dto.FilmDTO;
import com.movie.core.service.*;
import com.movie.core.utils.FormUtil;
import com.movie.core.utils.RequestUtil;
import com.movie.core.utils.WebCommonUtil;
import com.movie.web.command.FilmCommand;
import com.movie.web.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller(value = "filmControllerOfAdmin")
public class FilmController {
    ResourceBundle bundle = ResourceBundle.getBundle("i18n/message_vn");

    @Autowired
    private IFilmService filmService;

    @Autowired
    private ICountryService countryService;
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private IFilmTypeService filmTypeService;
    @Autowired
    private IDirectorService directorService;
    @Autowired
    private IActorService actorService;

    @RequestMapping(value = {"/admin/film/list"}, method = RequestMethod.GET)
    public String showFilmList(Model model, HttpServletRequest request) {
        FilmCommand command = FormUtil.populate(FilmCommand.class, request);
        RequestUtil.initSearchBeanManual(command);
        executeSearchFilm(command);
        if (command.getMessage() != null) {
            WebCommonUtil.addRedirectMessage(model, getMapMessage(), command.getMessage());
        }
        setAttribute(model);
        model.addAttribute(WebConstant.LIST_ITEM, command);
        return "views/admin/film/list";
    }

    private void setAttribute(Model model) {
        List<Integer> years = new ArrayList<Integer>();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 0; i < 5; i++) {
            years.add((year));
            year--;
        }
        model.addAttribute(WebConstant.COUNTRY_LIST, countryService.findAll());
        model.addAttribute(WebConstant.CATEGORY_LIST, categoryService.findAll());
        model.addAttribute(WebConstant.FILM_TYPE_LIST, filmTypeService.findAll());
        model.addAttribute(WebConstant.YEARS, years);
    }

    @RequestMapping(value = {"/admin/film/show"}, method = RequestMethod.GET)
    public ModelAndView showFilmProfile(@RequestParam Long id) {
        FilmDTO filmDTO = null;
        if (id != null) {
            if (SecurityUtils.getEmployeeAuthorities().contains("ADMIN")) {
                filmDTO = filmService.findOneById(id);
            } else if (SecurityUtils.getEmployeeAuthorities().contains("POSTER")) {
                filmDTO = filmService.findOneById(id, 1);
            }
        }
        ModelAndView mav = new ModelAndView("admin/film/profile");
        mav.addObject(WebConstant.FORM_ITEM, filmDTO);
        return mav;
    }

    @RequestMapping(value = "/admin/film/edit", method = RequestMethod.GET)
    public String showFilmEdit(Model model, @RequestParam(value = "filmId", required = false) Long id,
                               @RequestParam(value = "message", required = false) String message) {
        if (message != null) {
            WebCommonUtil.addRedirectMessage(model, getMapMessage(), message);
        }
        FilmDTO filmDTO = new FilmDTO();
        if (id != null) {
            if (SecurityUtils.getEmployeeAuthorities().contains(WebConstant.ROLE_ADMIN)) {
                filmDTO = filmService.findOneById(id);
            } else if (SecurityUtils.getEmployeeAuthorities().contains(WebConstant.ROLE_POSTER)) {
                filmDTO = filmService.findOneById(id, 1);
            }
        }
        model.addAttribute(WebConstant.FORM_ITEM, filmDTO);
        model.addAttribute("countries", countryService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("filmTypes", filmTypeService.findAll());
        model.addAttribute("directors", directorService.findAll());
        model.addAttribute("actors", actorService.findAll());
        return "views/admin/film/edit";
    }

    @RequestMapping(value = "/admin/film/trailer", method = RequestMethod.GET)
    public ModelAndView showFilmTrailerEdit(@RequestParam(value = "id", required = false) Long id,
                                            @RequestParam(value = "message", required = false) String message,
                                            HttpServletRequest request) {
        if (message != null) {
            WebCommonUtil.addRedirectMessage(request, getMapMessage(), message);
        }
        FilmDTO filmDTO = new FilmDTO();
        if (id != null) {
            if (SecurityUtils.getEmployeeAuthorities().contains("ADMIN")) {
                filmDTO = filmService.findOneById(id);
            } else if (SecurityUtils.getEmployeeAuthorities().contains("POSTER")) {
                filmDTO = filmService.findOneById(id, 1);
            }
        }
        ModelAndView mav = new ModelAndView("admin/film/trailerEdit");
        mav.addObject(WebConstant.FORM_ITEM, filmDTO);
        return mav;
    }

    public void executeSearchFilm(FilmCommand command) {
        String userName = null;
        if (SecurityUtils.getEmployeeAuthorities().contains(WebConstant.ROLE_ADMIN)) {
            userName = "ADMIN";
        } else if (SecurityUtils.getEmployeeAuthorities().contains(WebConstant.ROLE_POSTER)) {
            userName = SecurityUtils.getPrincipal().getUsername();
        }
        List<FilmDTO> filmDTOS = filmService.findByProperties(command.getSearch(), command.getFilmType(), command.getCategory(), command.getCountry(), command.getYear(), userName,
                command.getPage(), command.getLimit(), command.getSortExpression(), command.getSortDirection());
        command.setListResult(filmDTOS);
        command.setTotalItems(filmService.getTotalItem(userName, command.getFilmType(), command.getCategory(), command.getCountry(), command.getYear(), command.getSearch()));
        command.setTotalPage((int) Math.ceil((double) command.getTotalItems() / command.getLimit()));
    }

    private Map<String, String> getMapMessage() {
        Map<String, String> mapValue = new HashMap<String, String>();
        mapValue.put(WebConstant.REDIRECT_ERROR, bundle.getString("label.message.error"));
        mapValue.put(WebConstant.REDIRECT_INSERT, bundle.getString("label.film.message.add.success"));
        mapValue.put(WebConstant.REDIRECT_DELETE, bundle.getString("label.film.message.delete.success"));
        mapValue.put(WebConstant.REDIRECT_UPDATE, bundle.getString("label.film.message.update.success"));
        return mapValue;
    }
}
