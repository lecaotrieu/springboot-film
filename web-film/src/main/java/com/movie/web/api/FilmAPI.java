package com.movie.web.api;

import com.movie.core.constant.CoreConstant;
import com.movie.core.dto.FilmDTO;
import com.movie.core.service.IFilmService;
import com.movie.core.utils.UploadUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController(value = "FilmAPI")
public class FilmAPI {

    @Autowired
    private IFilmService filmService;

    @PostMapping("/api/admin/film")
    public FilmDTO createFilm(@RequestBody FilmDTO filmDTO) {
        return filmService.save(filmDTO);
    }

    @PostMapping("/api/admin/film/image")
    public void saveImageFilm(@RequestParam("img1") MultipartFile img1, @RequestParam("id") Long id, @RequestParam("img2") MultipartFile img2) throws Exception {
        try {
            if (img1.getSize() > CoreConstant.IMAGE_UPLOAD_MAX || img2.getSize() > CoreConstant.IMAGE_UPLOAD_MAX) {
                throw new Exception();
            } else {
                String uploadDir = CoreConstant.FOLDER_UPLOAD + File.separator + CoreConstant.FILM_IMAGES + File.separator + id;
                if (!img1.isEmpty()) {
                    String fileName1 = org.springframework.util.StringUtils.cleanPath(img1.getOriginalFilename());
                    fileName1 = "film_img1_" + id + getFieldName(fileName1);
                    UploadUtil.saveFile(uploadDir, fileName1, img1);
                    filmService.updateImg1(id, fileName1);
                }
                if (!img2.isEmpty()) {
                    String fileName2 = org.springframework.util.StringUtils.cleanPath(img2.getOriginalFilename());
                    fileName2 = "film_img2_" + id + getFieldName(fileName2);
                    UploadUtil.saveFile(uploadDir, fileName2, img2);
                    filmService.updateImg2(id, fileName2);
                }
            }
        } catch (Exception e) {
            throw new Exception();
        }
    }


    private String getFieldName(String fileName) {
        return fileName.substring(fileName.length() - 4);
    }

    @PutMapping("/api/admin/film/trailer")
    public void updateFilmTrailer(@RequestParam("video") MultipartFile file, @RequestParam("id") Long id, @RequestParam("trailerYoutube") String trailerYoutube) {
        try {
            filmService.updateTrailer(id, file, trailerYoutube);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PutMapping("/api/admin/film")
    public Long updateFilm(@RequestBody FilmDTO filmDTO) {
        return filmService.save(filmDTO).getId();
    }

    @PutMapping("/api/film/view")
    public void updateFilmView(@RequestBody Long filmId) {
        filmService.updateView(filmId);
    }

    @PutMapping("/api/admin/film/status")
    public boolean updateFilmStatus(@RequestBody FilmDTO filmDTO) {
        return filmService.updateFilmStatus(filmDTO);
    }

    @DeleteMapping("/api/admin/film")
    public void deleteFilm(@RequestBody Long[] ids) throws Exception {
        filmService.deleteById(ids);
    }
}
