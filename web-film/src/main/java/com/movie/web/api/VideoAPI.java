package com.movie.web.api;


import com.movie.core.constant.CoreConstant;
import com.movie.core.dto.UserDTO;
import com.movie.core.dto.VideoDTO;
import com.movie.core.service.IVideoService;
import com.movie.core.utils.UploadUtil;
import com.movie.web.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
public class VideoAPI {

    @Autowired
    private IVideoService videoService;

    @PutMapping("/api/admin/video")
    public Long updateVideo(@RequestBody VideoDTO videoDTO) throws Exception {
        return videoService.save(videoDTO).getId();
    }

    @PutMapping("/api/video/view")
    public void updateFilmView(@RequestBody Long videoId) {
        videoService.updateView(videoId);
    }

    @PutMapping("/api/admin/video/status")
    public boolean updateVideoStatusOfAdmin(@RequestBody VideoDTO videoDTO) {
        return videoService.updateVideoStatus(videoDTO);
    }

    @DeleteMapping("/api/admin/video")
    public void deleteVideo(@RequestBody Long[] ids) throws Exception {
        videoService.deleteById(ids);
    }

    @DeleteMapping("/api/video")
    public void deleteVideoOfUser(@RequestBody Long[] ids) throws Exception {
        videoService.deleteById(ids);
    }
    @PostMapping("/api/user/video")
    public String uploadVideoOfUser(@RequestParam(value = "img", required = false) MultipartFile image,
                                    @RequestParam(value = "video", required = false) MultipartFile video,
                                    @RequestParam(value = "brief") String brief,
                                    @RequestParam("title") String title,
                                    @RequestParam(value = "status") Integer status,
                                    @RequestParam(value = "id", required = false) Long id) throws Exception {
        Long userId = SecurityUtils.getUserPrincipal().getId();
        if (id != null) {
            checkUser(userId, id);
        }
        if (image != null && !image.isEmpty() && image.getSize() > CoreConstant.IMAGE_UPLOAD_MAX) {
            return "image_over_size";
        }
        if (video != null && !video.isEmpty() && video.getSize() > CoreConstant.VIDEO_UPLOAD_MAX) {
            return "video_over_size";
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        VideoDTO videoDTO = new VideoDTO();
        videoDTO.setUser(userDTO);
        videoDTO.setName(title);
        videoDTO.setBrief(brief);
        videoDTO.setStatus(status);
        if (id != null) {
            videoDTO.setId(id);
        } else {
            videoDTO = videoService.save(videoDTO);
        }
        if (image != null) {
            String imageName = uploadImageVideo(videoDTO.getId(), image);
            videoDTO.setImage(imageName);
        }
        if (video != null) {
            String videoName = videoService.uploadVideoToDrive(videoDTO.getId(), video);
            videoDTO.setVideo(videoName);
        }
        videoService.save(videoDTO);
        return "success";
    }

    private void checkUser(Long userId, Long videoId) throws Exception {
        boolean rs = videoService.checkUser(userId, videoId);
        if (rs == false) throw new Exception("Kh??ng ph???i user");
    }

    private String getFieldName(String fileName) {
        return fileName.substring(fileName.length() - 4);
    }

    @Autowired
    private UploadUtil uploadUtil;

    private String uploadImageVideo(Long videoId, MultipartFile image) throws Exception {
        String fileName = org.springframework.util.StringUtils.cleanPath(image.getOriginalFilename());
        fileName = "video_img_" + videoId + getFieldName(fileName);
        String uploadDir = CoreConstant.FOLDER_UPLOAD + File.separator + CoreConstant.VIDEO_IMAGES + File.separator + videoId;
        uploadUtil.saveFile(uploadDir, fileName, image);
        return fileName;
    }


}
