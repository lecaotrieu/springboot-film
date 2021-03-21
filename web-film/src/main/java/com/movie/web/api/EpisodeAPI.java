package com.movie.web.api;

import com.movie.core.dto.EpisodeDTO;
import com.movie.core.dto.FilmDTO;
import com.movie.core.service.IDriveService;
import com.movie.core.service.IEpisodeService;
import com.movie.core.utils.UploadUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController(value = "EpisodeAPI")
public class EpisodeAPI {
    @Autowired
    private IEpisodeService episodeService;

    @Autowired
    private IDriveService driveService;

    /*  titleValue.add("film.id");
            titleValue.add("name");
            titleValue.add("episodeCode");
            titleValue.add("id");
            titleValue.add("status");*/
    @PostMapping("/api/admin/film/episode")
    public EpisodeDTO saveEpisode(@RequestParam(value = "video", required = false) MultipartFile video, @RequestParam("filmId") Long filmId,
                                  @RequestParam(value = "name", required = false) String name,
                                  @RequestParam(value = "episodeCode", required = false) Integer episodeCode,
                                  @RequestParam("status") Integer status) {

        EpisodeDTO episodeDTO = setEpisode(null, name, episodeCode, status, filmId);
        if (video.getSize() > 0) {
            episodeDTO.setVideo(video);
        }
        return episodeService.save(episodeDTO);
    }

    private EpisodeDTO setEpisode(Long id, String name, Integer episodeCode, Integer status, Long filmId) {
        EpisodeDTO episodeDTO = new EpisodeDTO();
        episodeDTO.setEpisodeCode(episodeCode);
        episodeDTO.setId(id);
        FilmDTO filmDTO = new FilmDTO();
        filmDTO.setId(filmId);
        episodeDTO.setFilm(filmDTO);
        episodeDTO.setName(name);
        episodeDTO.setStatus(status);
        return episodeDTO;
    }

    @PutMapping("/api/admin/film/episode")
    public Long updateEpisode(@RequestParam(value = "video", required = false) MultipartFile video, @RequestParam("filmId") Long filmId,
                              @RequestParam(value = "name", required = false) String name,
                              @RequestParam(value = "episodeCode", required = false) Integer episodeCode,
                              @RequestParam("id") Long id, @RequestParam("status") Integer status) {
        EpisodeDTO episodeDTO =setEpisode(id, name, episodeCode, status, filmId);
        if (video.getSize() > 0) {
            episodeDTO.setVideo(video);
        }
        episodeService.save(episodeDTO);
        return episodeDTO.getId();
    }

    @PutMapping("/api/admin/film/episode/status")
    public Long updateEpisodeStatus(@RequestBody EpisodeDTO episodeDTO) {
        return episodeService.updateStatus(episodeDTO.getId(), episodeDTO.getStatus());
    }

    @DeleteMapping("/api/admin/film/episode")
    public void deleteEpisode(@RequestBody Long[] ids) {
        episodeService.delete(ids);
    }
}

