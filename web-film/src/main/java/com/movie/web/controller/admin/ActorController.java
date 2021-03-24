package com.movie.web.controller.admin;

import com.movie.core.constant.CoreConstant;
import com.movie.core.constant.WebConstant;
import com.movie.core.dto.ActorDTO;
import com.movie.core.service.IActorService;
import com.movie.core.utils.FormUtil;
import com.movie.core.utils.RequestUtil;
import com.movie.core.utils.UploadUtil;
import com.movie.core.utils.WebCommonUtil;
import com.movie.web.command.ActorCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

@Controller(value = "ActorControllerOfAdmin")
public class ActorController {
    ResourceBundle bundle = ResourceBundle.getBundle("i18n/message_vn");

    @Autowired
    private IActorService actorService;

    @RequestMapping(value = {"/admin/actor/list"}, method = RequestMethod.GET)
    public String showActorList(Model model, @RequestParam(value = "message", required = false) String message,
                                HttpServletRequest request) {
        if (message != null) {
            WebCommonUtil.addRedirectMessage(request, getMapMessage(), message);
        }
        List<ActorDTO> actorDTOS = actorService.findAll();
        model.addAttribute(WebConstant.LIST_ITEM, actorDTOS);
        return "/views/admin/actor/list";
    }

    @RequestMapping(value = {"/admin/actor/edit"}, method = RequestMethod.POST)
    public String postActor(ActorDTO actorDTO, @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            actorDTO = actorService.save(actorDTO);
            if (file.getSize() > CoreConstant.IMAGE_UPLOAD_MAX) {
                return "redirect:/admin/actor/list?message=over_size";
            } else {
                if (!file.isEmpty()) {
                    String fileName = org.springframework.util.StringUtils.cleanPath(file.getOriginalFilename());
                    fileName = "actor_avatar_" + actorDTO.getId() + getFieldName(fileName);
                    String uploadDir = CoreConstant.FOLDER_UPLOAD + File.separator + CoreConstant.ACTOR_PHOTOS + File.separator + actorDTO.getId();
                    UploadUtil.saveFile(uploadDir, fileName, file);
                    actorService.updateActorAvatar(actorDTO.getId(), fileName);
                }
                return "redirect:/admin/actor/list?message=redirect_update";
            }

        } catch (Exception e) {
            return "redirect:/admin/actor/list?message=redirect_error";
        }
    }

    private String getFieldName(String fileName) {
        return fileName.substring(fileName.length() - 4);
    }

    private Map<String, String> getMapMessage() {
        Map<String, String> mapValue = new HashMap<String, String>();
        mapValue.put(WebConstant.REDIRECT_ERROR, bundle.getString("label.message.error"));
        mapValue.put(WebConstant.REDIRECT_INSERT, bundle.getString("label.actor.message.add.success"));
        mapValue.put(WebConstant.REDIRECT_DELETE, bundle.getString("label.actor.message.delete.success"));
        mapValue.put(WebConstant.REDIRECT_UPDATE, bundle.getString("label.actor.message.update.success"));
        return mapValue;
    }
}
