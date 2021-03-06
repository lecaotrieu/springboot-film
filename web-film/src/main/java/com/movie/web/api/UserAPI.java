package com.movie.web.api;

import com.movie.core.constant.CoreConstant;
import com.movie.core.constant.WebConstant;
import com.movie.core.dto.UserDTO;
import com.movie.core.service.IUserService;
import com.movie.core.utils.UploadUtil;
import com.movie.web.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;

@RestController(value = "UserAPIOfAdmin")
public class UserAPI {
    @Autowired
    private IUserService userService;

    @PostMapping("/api/admin/user")
    public UserDTO createUser(@RequestBody UserDTO userDTO) throws Exception {
        return userService.save(userDTO);
    }

    @PostMapping("/api/user/register")
    public UserDTO registerUser(@RequestBody UserDTO userDTO) throws Exception {
        return userService.save(userDTO);
    }

    @PutMapping("/api/admin/user/status")
    public boolean updateUserStatus(@RequestBody UserDTO userDTO) {
        userService.updateUserStatus(userDTO);
        return true;
    }

    @PutMapping("/api/admin/user/password")
    public Long updateUserPassword(@RequestBody UserDTO userDTO) throws Exception {
        if (userDTO.getPassword().length() < 6) throw new Exception();
        userService.updatePassword(userDTO.getId(), userDTO.getPassword());
        return userDTO.getId();
    }

    @PutMapping("/api/user/password")
    public Long updateUserPasswordOfUser(@RequestBody UserDTO userDTO) throws Exception {
        if (SecurityUtils.getUserAuthorities().contains(WebConstant.ROLE_USER)) {
            userDTO.setId(SecurityUtils.getUserPrincipal().getId());
            userService.updatePassword(userDTO);
            return userDTO.getId();
        }
        throw new Exception();
    }


    @PutMapping("/api/user")
    public Long updateUserOfUser(@RequestBody UserDTO userDTO) throws Exception {
        userDTO = userService.save(userDTO);
        return userDTO.getId();
    }

    @PutMapping("/api/admin/user")
    public Long updateUser(@RequestBody UserDTO userDTO) throws Exception {
        userDTO = userService.save(userDTO);
        return userDTO.getId();
    }

    @PostMapping("/api/admin/user/photo")
    public ModelAndView updateUserPhoto(@RequestParam(value = "id", required = false) Long id, @RequestParam("img") MultipartFile file) {
        ModelAndView mav = new ModelAndView();
        try {
            if (file.getSize() > CoreConstant.IMAGE_UPLOAD_MAX) {
                mav.setViewName("redirect:/admin/user/photo/edit?id=" + id + "&message=over_size");
            } else {
                if (!file.isEmpty()) {
                    String fileName = org.springframework.util.StringUtils.cleanPath(file.getOriginalFilename());
                    fileName = "film_img_" + id + getFieldName(fileName);
                    String uploadDir = CoreConstant.FOLDER_UPLOAD + File.separator + CoreConstant.USER_PHOTOS + File.separator + id;
                    uploadUtil.saveFile(uploadDir, fileName, file);
                    userService.updatePhoto(id, fileName);
                }
                mav.setViewName("redirect:/admin/user/photo/edit?id=" + id + "&message=redirect_update");
            }

        } catch (Exception e) {
            mav.setViewName("redirect:/admin/user/photo/edit?id=" + id + "&message=redirect_error");
        }
        return mav;
    }

    private String getFieldName(String fileName) {
        return fileName.substring(fileName.length() - 4);
    }

    @Autowired
    private UploadUtil uploadUtil;

    @PostMapping("/api/user/photo")
    public String updatePhotoOfUser(@RequestParam("img") MultipartFile file) throws Exception {
        if (file.getSize() > CoreConstant.IMAGE_UPLOAD_MAX) {
            return "over_size";
        } else {
            if (!file.isEmpty()) {
                Long id = SecurityUtils.getUserPrincipal().getId();
                String fileName = org.springframework.util.StringUtils.cleanPath(file.getOriginalFilename());
                fileName = "film_img_" + id + getFieldName(fileName);
                String uploadDir = CoreConstant.FOLDER_UPLOAD + File.separator + CoreConstant.USER_PHOTOS + File.separator + id;
                uploadUtil.saveFile(uploadDir, fileName, file);
                userService.updatePhoto(id, fileName);
            }
            return "success";
        }
    }


    @DeleteMapping("/api/admin/user")
    public void deleteUser(@RequestBody Long[] ids) {
        userService.deleteById(ids);
    }
}
