package com.movie.web.controller.admin;

import com.movie.core.constant.CoreConstant;
import com.movie.core.constant.WebConstant;
import com.movie.core.dto.EmployeeDTO;
import com.movie.core.service.ICountryService;
import com.movie.core.service.IEmployeeService;
import com.movie.core.service.IRoleService;
import com.movie.core.utils.FormUtil;
import com.movie.core.utils.RequestUtil;
import com.movie.core.utils.UploadUtil;
import com.movie.core.utils.WebCommonUtil;
import com.movie.web.command.EmployeeCommand;
import com.movie.web.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;

@Controller(value = "EmployeeControllerOfAdmin")
public class EmployeeController {

    @Autowired
    private IEmployeeService employeeService;

    ResourceBundle bundle = ResourceBundle.getBundle("i18n/message_vn");

    @RequestMapping(value = "/admin/employee/list", method = RequestMethod.GET)
    public String showEmployeeList(Model model, @ModelAttribute(WebConstant.LIST_ITEM) EmployeeCommand command, HttpServletRequest request) {
        command = FormUtil.populate(EmployeeCommand.class, request);
        RequestUtil.initSearchBean(request, command);
        executeSearchEmployee(command);
        if (command.getMessage() != null) {
            WebCommonUtil.addRedirectMessage(model, getMapMessage(), command.getMessage());
        }
        EmployeeDTO employeeDTO = new EmployeeDTO();
        model.addAttribute(WebConstant.LIST_ITEM, command);
        model.addAttribute("e", employeeDTO);

        return "/views/admin/employee/list";
    }

    @Autowired
    private ICountryService countryService;

    @Autowired
    private IRoleService roleService;

    @RequestMapping(value = {"/admin/employee/edit", "/admin/personal/information"}, method = RequestMethod.GET)
    public String showEmployeeEdit(Model model, @RequestParam(value = "id", required = false) Long id,
                                   @RequestParam(value = "message", required = false) String message,
                                   HttpServletRequest request) {
        if (message != null) {
            WebCommonUtil.addRedirectMessage(model, getMapMessage(), message);
        }
        EmployeeDTO employeeDTO = new EmployeeDTO();
        if (SecurityUtils.getEmployeeAuthorities().contains(WebConstant.ROLE_ADMIN)) {
            if (id != null) {
                employeeDTO = employeeService.findOneById(id);
            } else if (request.getRequestURL().toString().endsWith("/admin/personal/information")) {
                employeeDTO = employeeService.findOneById(SecurityUtils.getPrincipal().getId());
            }
        } else {
            if (id != null) {
                return "redirect:/admin/personal/information";
            } else {
                employeeDTO = employeeService.findOneById(SecurityUtils.getPrincipal().getId());
            }
        }
        model.addAttribute("roles", roleService.findAllNotAdmin());
        model.addAttribute("countries", countryService.findAll());
        model.addAttribute(WebConstant.FORM_ITEM, employeeDTO);
        return "views/admin/employee/edit";
    }

    @RequestMapping(value = {"/admin/employee/photo/edit"}, method = RequestMethod.GET)
    public String showEmployeePhotoEdit(Model model, @RequestParam(value = "id") Long id,
                                        @RequestParam(value = "message", required = false) String message) {
        if (message != null) {
            WebCommonUtil.addRedirectMessage(model, getMapMessage(), message);
        }
        try {
            EmployeeDTO employeeDTO = employeeService.findOneById(id);
            model.addAttribute(WebConstant.FORM_ITEM, employeeDTO);
        } catch (Exception e) {
            return "redirect:/admin/employee/list?message=redirect_error";
        }
        return "views/admin/employee/edit_avatar";
    }

    @Autowired
    private UploadUtil uploadUtil;

    @PostMapping({"/api/admin/employee/photo", "/api/admin/personal/photo"})
    public String updateEmployeePhoto(@RequestParam(value = "id", required = false) Long id, @RequestParam("img") MultipartFile file, HttpServletRequest request) {
        String urlRq;
        if (request.getRequestURL().toString().endsWith("/api/admin/personal/photo")) {
            urlRq = "/admin/personal/photo?message=";
        } else {
            urlRq = "/admin/employee/photo/edit?id=" + id + "&message=";
        }
        try {
            if (!file.isEmpty()) {
                if (file.getSize() > CoreConstant.IMAGE_UPLOAD_MAX) {
                    return "redirect:" + urlRq + "over_size";
                }
                String fileName = StringUtils.cleanPath(file.getOriginalFilename());
                fileName = "employee_photo_" + id + getFieldName(fileName);
                String uploadDir = CoreConstant.FOLDER_UPLOAD + File.separator + CoreConstant.EMPLOYEE_PHOTOS + File.separator + id;
                uploadUtil.saveFile(uploadDir, fileName, file);
                employeeService.updatePhoto(id, fileName);
            }
            return "redirect:" + urlRq + "redirect_update";
        } catch (Exception e) {
            return "redirect:" + urlRq + "redirect_error";
        }
    }

    private String getFieldName(String fileName) {
        return fileName.substring(fileName.length() - 4);
    }

    @RequestMapping(value = {"/admin/employee/profile"}, method = RequestMethod.GET)
    public String showEmployeeProfile(Model model, @RequestParam(value = "id") Long id,
                                      @RequestParam(value = "message", required = false) String message) {
        EmployeeDTO employeeDTO;
        if (message != null) {
            WebCommonUtil.addRedirectMessage(model, getMapMessage(), message);
        }
        employeeDTO = employeeService.findOneById(id);
        model.addAttribute(WebConstant.FORM_ITEM, employeeDTO);
        return "views/admin/employee/profile";
    }

    @RequestMapping(value = {"/admin/personal/photo"}, method = RequestMethod.GET)
    public String showPersonalPhoto(Model model, @RequestParam(value = "message", required = false) String message) {
        if (message != null) {
            WebCommonUtil.addRedirectMessage(model, getMapMessage(), message);
        }
        try {
            EmployeeDTO employeeDTO = employeeService.findOneById(SecurityUtils.getPrincipal().getId());
            model.addAttribute(WebConstant.FORM_ITEM, employeeDTO);
        } catch (Exception e) {
            return "redirect:/admin/employee/list?message=redirect_error";
        }
        return "views/admin/employee/edit_avatar";
    }


    public void executeSearchEmployee(EmployeeCommand command) {
        if (SecurityUtils.getEmployeeAuthorities().contains("ROLE_ADMIN")) {
            List<String> roleCodes = new ArrayList<String>();
            if (command.getRole() != null && !command.getRole().equals(WebConstant.ROLE_ADMIN)) {
                roleCodes.add(command.getRole());
            } else {
                roleCodes.add(WebConstant.ROLE_MANAGER);
                roleCodes.add(WebConstant.ROLE_POSTER);
            }
            List<EmployeeDTO> employeeDTOS = employeeService.findByProperties(command.getSearch(), roleCodes,
                    command.getPage(), command.getLimit(), command.getSortExpression(), command.getSortDirection());
            command.setListResult(employeeDTOS);
            command.setTotalItems(employeeService.getTotalItem(roleCodes, command.getSearch()));
            command.setTotalPage((int) Math.ceil((double) command.getTotalItems() / command.getLimit()));
        }
    }

    private Map<String, String> getMapMessage() {
        Map<String, String> mapValue = new HashMap<String, String>();
        mapValue.put(WebConstant.REDIRECT_ERROR, bundle.getString("label.message.error"));
        mapValue.put("over_size", bundle.getString("label.message.over.size"));
        mapValue.put(WebConstant.REDIRECT_INSERT, bundle.getString("label.employee.message.add.success"));
        mapValue.put(WebConstant.REDIRECT_DELETE, bundle.getString("label.employee.message.delete.success"));
        mapValue.put(WebConstant.REDIRECT_UPDATE, bundle.getString("label.employee.message.update.success"));
        return mapValue;
    }
}
