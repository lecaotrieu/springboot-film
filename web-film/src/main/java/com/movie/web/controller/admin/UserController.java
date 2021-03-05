package com.movie.web.controller.admin;

import com.movie.core.constant.CoreConstant;
import com.movie.core.constant.WebConstant;
import com.movie.core.dto.UserDTO;
import com.movie.core.service.IEvaluateService;
import com.movie.core.service.IUserService;
import com.movie.core.utils.FormUtil;
import com.movie.core.utils.RequestUtil;
import com.movie.core.utils.WebCommonUtil;
import com.movie.web.command.UserCommand;
import com.movie.web.utils.SecurityUtils;
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

@Controller(value = "UserControllerOfAdmin")
public class UserController {
    ResourceBundle bundle = ResourceBundle.getBundle("i18n/message_vn");
    @Autowired
    private IUserService userService;

    @RequestMapping(value = {"/admin/user/list"}, method = RequestMethod.GET)
    public String showUserList(Model model, HttpServletRequest request) {
        UserCommand command = FormUtil.populate(UserCommand.class, request);
        RequestUtil.initSearchBeanManual(command);
        executeSearchUser(command);
        if (command.getMessage() != null)
            WebCommonUtil.addRedirectMessage(model, getMapMessage(), command.getMessage());
        model.addAttribute(WebConstant.LIST_ITEM, command);
        return "/views/admin/user/list";
    }

    private void executeSearchUser(UserCommand command) {
        if (SecurityUtils.getEmployeeAuthorities().contains(WebConstant.ROLE_ADMIN) || SecurityUtils.getEmployeeAuthorities().contains(WebConstant.ROLE_MANAGER)) {
            List<UserDTO> userDTOS = userService.findByProperties(command.getSearch(), command.getPage(), command.getLimit(), command.getSortExpression(), command.getSortDirection());
            command.setListResult(userDTOS);
            command.setTotalItems(userService.getTotalItem(command.getSearch()));
            command.setTotalPage((int) Math.ceil((double) command.getTotalItems() / command.getLimit()));
        }
    }

    @RequestMapping(value = {"/admin/user/edit"}, method = RequestMethod.GET)
    public String showUserEdit(Model model, @RequestParam(value = "id", required = false) Long id,
                               @RequestParam(value = "message", required = false) String message,
                               HttpServletRequest request) {
        if (message != null) {
            WebCommonUtil.addRedirectMessage(request, getMapMessage(), message);
        }
        UserDTO userDTO = new UserDTO();
        if (id != null) {
            try {
                userDTO = userService.findOneById(id);
            } catch (Exception e) {
                return "redirect:/admin/user/list?message=redirect_error";
            }
        }
        model.addAttribute(WebConstant.FORM_ITEM, userDTO);
        return "views/admin/user/edit";
    }

    @RequestMapping(value = "/admin/user/photo/edit", method = RequestMethod.GET)
    public String showUserPhotoEdit(Model model, @RequestParam(value = "id") Long id,
                                    @RequestParam(value = "message", required = false) String message,
                                    HttpServletRequest request) {
        if (message != null) {
            WebCommonUtil.addRedirectMessage(request, getMapMessage(), message);
        }
        try {
            UserDTO userDTO = userService.findOneById(id);
            model.addAttribute(WebConstant.FORM_ITEM, userDTO);
        } catch (Exception e) {
            return "redirect:/admin/user/list?message=redirect_error";
        }
        return "views/admin/user/edit_avatar";
    }

    @Autowired
    private IEvaluateService evaluateService;

    @RequestMapping(value = "/admin/user/profile", method = RequestMethod.GET)
    public String showUserProfile(Model model, @RequestParam(value = "id") Long id) {
        try {
            UserDTO userDTO = userService.findOneById(id);
            userDTO.setEvaluates(evaluateService.findAllByUserId(id, CoreConstant.ACTIVE_STATUS));
            model.addAttribute(WebConstant.FORM_ITEM, userDTO);
        } catch (Exception e) {
            return "redirect:/admin/user/list?message=redirect_error";
        }
        return "views/admin/user/profile";
    }

    private Map<String, String> getMapMessage() {
        Map<String, String> mapValue = new HashMap<String, String>();
        mapValue.put(WebConstant.REDIRECT_ERROR, bundle.getString("label.message.error"));
        mapValue.put(WebConstant.REDIRECT_INSERT, bundle.getString("label.user.message.add.success"));
        mapValue.put("over_size", bundle.getString("label.message.over.size"));
        mapValue.put(WebConstant.REDIRECT_DELETE, bundle.getString("label.user.message.delete.success"));
        mapValue.put(WebConstant.REDIRECT_UPDATE, bundle.getString("label.user.message.update.success"));
        return mapValue;
    }

}
