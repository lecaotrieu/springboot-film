package com.movie.web.controller.admin;

import com.movie.core.constant.WebConstant;
import com.movie.core.dto.DirectorDTO;
import com.movie.core.service.IDirectorService;
import com.movie.core.utils.WebCommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

@Controller(value = "DirectorControllerOfAdmin")
public class DirectorController {
    ResourceBundle bundle = ResourceBundle.getBundle("i18n/message_vn");

    @Autowired
    private IDirectorService directorService;

    @RequestMapping(value = {"/admin/director/list"}, method = RequestMethod.GET)
    public String showDirectorList(Model model, @RequestParam(value = "message", required = false) String message,
                                   HttpServletRequest request) {
        if (message != null) {
            WebCommonUtil.addRedirectMessage(request, getMapMessage(), message);
        }
        List<DirectorDTO> directorDTOS = directorService.findAll();
        model.addAttribute(WebConstant.LIST_ITEM, directorDTOS);
        return "/views/admin/director/list";
    }

    private Map<String, String> getMapMessage() {
        Map<String, String> mapValue = new HashMap<String, String>();
        mapValue.put(WebConstant.REDIRECT_ERROR, bundle.getString("label.message.error"));
        mapValue.put(WebConstant.REDIRECT_INSERT, bundle.getString("label.director.message.add.success"));
        mapValue.put(WebConstant.REDIRECT_DELETE, bundle.getString("label.director.message.delete.success"));
        mapValue.put(WebConstant.REDIRECT_UPDATE, bundle.getString("label.director.message.update.success"));
        return mapValue;
    }
}
