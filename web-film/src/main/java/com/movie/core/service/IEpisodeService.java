package com.movie.core.service;

import com.movie.core.dto.EpisodeDTO;
import org.apache.commons.fileupload.FileItem;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface IEpisodeService {
    void updateVideo(Long id, FileItem video) throws IOException;

    void updateVideo(Long id, MultipartFile video) throws IOException;

    Long updateStatus(Long id, Integer status);

    EpisodeDTO save(EpisodeDTO episodeDTO);

    void delete(Long[] ids);

    EpisodeDTO findOneByFilmAndCode(String filmCode, Integer code);

    EpisodeDTO findOneById(Long id);

    EpisodeDTO findOneById(Long id, Integer status);

    List<EpisodeDTO> findAllByFilmId(Long id, Integer status);

    List<EpisodeDTO> findAllByFilmId(Long id);

    List<EpisodeDTO> findAllByProperties(Long id, String search, boolean status, int page, int limit, String sortBy, String sortDrc);

    int countAllByProperties(Long id, String search, boolean status);

    boolean checkEmployeeCreate(String username, Long id);
}
