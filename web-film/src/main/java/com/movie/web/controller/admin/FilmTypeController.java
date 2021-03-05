package com.movie.web.controller.admin;

import com.movie.core.constant.WebConstant;
import com.movie.core.dto.FilmTypeDTO;
import com.movie.core.service.IFilmTypeService;
import com.movie.core.utils.WebCommonUtil;
import com.movie.web.command.FilmTypeCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

@Controller(value = "FilmTypeControllerOfAdmin")
public class FilmTypeController {
    ResourceBundle bundle = ResourceBundle.getBundle("i18n/message_vn");

    @Autowired
    private IFilmTypeService filmTypeService;

    @RequestMapping(value = {"/admin/film-type/list"}, method = RequestMethod.GET)
    public String showCategoryList(Model model, @RequestParam(value = "message", required = false) String message,
                                   HttpServletRequest request) {
        if (message != null) {
            WebCommonUtil.addRedirectMessage(request, getMapMessage(), message);
        }
        List<FilmTypeDTO> filmTypeDTOS = filmTypeService.findAll();
        model.addAttribute(WebConstant.LIST_ITEM, filmTypeDTOS);
        return "/views/admin/film-type/list";
    }

    private Map<String, String> getMapMessage() {
        Map<String, String> mapValue = new HashMap<String, String>();
        mapValue.put(WebConstant.REDIRECT_ERROR, bundle.getString("label.message.error"));
        mapValue.put(WebConstant.REDIRECT_INSERT, bundle.getString("label.film.type.message.add.success"));
        mapValue.put(WebConstant.REDIRECT_DELETE, bundle.getString("label.film.type.message.delete.success"));
        mapValue.put(WebConstant.REDIRECT_UPDATE, bundle.getString("label.film.type.message.update.success"));
        return mapValue;
    }
}
