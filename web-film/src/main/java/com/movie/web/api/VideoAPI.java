package com.movie.web.api;


import com.movie.core.dto.FilmDTO;
import com.movie.core.dto.VideoDTO;
import com.movie.core.service.IVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VideoAPI {

    @Autowired
    private IVideoService videoService;

    @PutMapping("/api/admin/video")
    public Long updateVideo(@RequestBody VideoDTO videoDTO) {
        return videoService.save(videoDTO).getId();
    }


    @PutMapping("/api/admin/video/status")
    public boolean updateFilmStatus(@RequestBody VideoDTO videoDTO) {
        return videoService.updateVideoStatus(videoDTO);
    }

    @DeleteMapping("/api/admin/video")
    public void deleteVideo(@RequestBody Long[] ids) throws Exception {
        videoService.deleteById(ids);
    }

}
