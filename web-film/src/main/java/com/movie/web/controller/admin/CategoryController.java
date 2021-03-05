package com.movie.web.controller.admin;

import com.movie.core.constant.WebConstant;
import com.movie.core.dto.CategoryDTO;
import com.movie.core.service.ICategoryService;
import com.movie.core.utils.FormUtil;
import com.movie.core.utils.RequestUtil;
import com.movie.core.utils.WebCommonUtil;
import com.movie.web.command.CategoryCommand;
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

@Controller(value = "CategoryControllerOfAdmin")
public class CategoryController {
    ResourceBundle bundle = ResourceBundle.getBundle("i18n/message_vn");

    @Autowired
    private ICategoryService categoryService;

    @RequestMapping(value = {"/admin/category/list"}, method = RequestMethod.GET)
    public String showCategoryList(Model model, @RequestParam(value = "message", required = false) String message,
                                   HttpServletRequest request) {
        if (message != null) {
            WebCommonUtil.addRedirectMessage(request, getMapMessage(), message);
        }
        List<CategoryDTO> categoryDTOS = categoryService.findAll();
        model.addAttribute(WebConstant.LIST_ITEM, categoryDTOS);
        return "/views/admin/category/list";
    }


    private Map<String, String> getMapMessage() {
        Map<String, String> mapValue = new HashMap<String, String>();
        mapValue.put(WebConstant.REDIRECT_ERROR, bundle.getString("label.message.error"));
        mapValue.put(WebConstant.REDIRECT_INSERT, bundle.getString("label.category.message.add.success"));
        mapValue.put(WebConstant.REDIRECT_DELETE, bundle.getString("label.category.message.delete.success"));
        mapValue.put(WebConstant.REDIRECT_UPDATE, bundle.getString("label.category.message.update.success"));
        return mapValue;
    }
}
