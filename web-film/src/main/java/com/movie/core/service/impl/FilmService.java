package com.movie.core.service.impl;

import com.google.api.services.drive.model.File;
import com.movie.core.constant.CoreConstant;
import com.movie.core.convert.ActorConvert;
import com.movie.core.dto.ActorDTO;
import com.movie.core.dto.CategoryDTO;
import com.movie.core.dto.FilmDTO;
import com.movie.core.entity.*;
import com.movie.core.repository.EpisodeRepository;
import com.movie.core.repository.EvaluateRepository;
import com.movie.core.repository.FilmRepository;
import com.movie.core.service.*;
import com.movie.core.service.utils.PagingUtils;
import com.movie.core.service.utils.StringGlobalUtils;
import com.movie.core.convert.FilmConvert;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FilmService implements IFilmService {
    @Autowired
    private FilmRepository filmRepository;
    @Autowired
    private EpisodeRepository episodeRepository;
    @Autowired
    private FilmConvert filmConvert;

    @Autowired
    private PagingUtils pagingUtils;


    public List<FilmDTO> findByProperties(String search, String filmType, String category, String country, String year, String userName, int page, int limit, String sortExpression, String sortDirection) {
        search = search.toLowerCase();
        Pageable pageable = pagingUtils.setPageable(page, limit, sortExpression, sortDirection);
        List<FilmEntity> filmEntities;
        if (!userName.equals("ADMIN")) {
            filmEntities = filmRepository.findAllByPosterAndProperties(userName, search, CoreConstant.ACTIVE_STATUS, filmType, category, country, year, pageable);
        } else {
            filmEntities = filmRepository.findAllByAdminAndProperties(search, search, filmType, category, country, year, pageable);
        }
        List<FilmDTO> filmDTOS = new ArrayList<FilmDTO>();
        for (FilmEntity entity : filmEntities) {
            Integer countEpisode;
            if (!userName.equals("ADMIN")) {
                countEpisode = episodeRepository.countAllByFilm_IdAndStatus(entity.getId(), CoreConstant.ACTIVE_STATUS);
            } else {
                countEpisode = episodeRepository.countAllByFilm_Id(entity.getId());
            }
            FilmDTO filmDTO = filmConvert.toDTO(entity);
            filmDTO.setEpisodesCount(countEpisode);
            filmDTOS.add(filmDTO);

        }
        return filmDTOS;
    }

    public List<FilmDTO> findByProperties(String search, String filmType, String category, String country, String year, int page, int limit, String sortExpression, String sortDirection) {
        search = search.toLowerCase();
        Pageable pageable = pagingUtils.setPageable(page, limit, sortExpression, sortDirection);
        List<FilmEntity> filmEntities;
        Integer status = 1;
        filmEntities = filmRepository.findAllByProperties(search, filmType, category, country, year, status, pageable);
        List<FilmDTO> filmDTOS = new ArrayList<FilmDTO>();
        for (FilmEntity entity : filmEntities) {
            FilmDTO filmDTO = filmConvert.toDTO(entity);
            filmDTOS.add(filmDTO);
        }
        return filmDTOS;
    }


    public List<FilmDTO> findByProperties(int page, int limit, String sortExpression, String sortDirection) {
        Pageable pageable = pagingUtils.setPageable(page, limit, sortExpression, sortDirection);
        List<FilmEntity> filmEntities;
        filmEntities = filmRepository.findAllByStatus(CoreConstant.ACTIVE_STATUS, pageable);
        List<FilmDTO> filmDTOS = new ArrayList<FilmDTO>();
        for (FilmEntity entity : filmEntities) {
            FilmDTO filmDTO = filmConvert.toDTO(entity);
            filmDTOS.add(filmDTO);
        }
        return filmDTOS;
    }

    public List<FilmDTO> findByProperties(Long userId, int like, int page, int limit, String sortExpression, String sortDirection) {
        Pageable pageable = pagingUtils.setPageable(page, limit, sortExpression, sortDirection);
        List<FilmEntity> filmEntities;
        filmEntities = filmRepository.findFilmFavoriteByUserId(userId, like, pageable);
        List<FilmDTO> filmDTOS = new ArrayList<FilmDTO>();
        for (FilmEntity entity : filmEntities) {
            FilmDTO filmDTO = filmConvert.toDTO(entity);
            filmDTOS.add(filmDTO);
        }
        return filmDTOS;
    }

    @Override
    public List<FilmDTO> findByUserId(Long userId, int follow, int page, int limit, String sortExpression, String sortDirection) {
        Pageable pageable = pagingUtils.setPageable(page, limit, sortExpression, sortDirection);
        List<FilmEntity> filmEntities;
        filmEntities = filmRepository.findFilmFollowByUserId(userId, follow, pageable);
        List<FilmDTO> filmDTOS = new ArrayList<FilmDTO>();
        for (FilmEntity entity : filmEntities) {
            FilmDTO filmDTO = filmConvert.toDTO(entity);
            filmDTOS.add(filmDTO);
        }
        return filmDTOS;
    }

    @Override
    public List<FilmDTO> findByProperties(Long actorId) {
        List<FilmEntity> filmEntities;
        filmEntities = filmRepository.findAllByActorID(actorId);
        List<FilmDTO> filmDTOS = new ArrayList<FilmDTO>();
        for (FilmEntity entity : filmEntities) {
            FilmDTO filmDTO = filmConvert.toDTO(entity);
            filmDTOS.add(filmDTO);
        }
        return filmDTOS;
    }

    public List<FilmDTO> findByProperties(String filmTypeCode, int page, int limit, String sortExpression, String sortDirection) {
        Pageable pageable = pagingUtils.setPageable(page, limit, sortExpression, sortDirection);
        List<FilmEntity> filmEntities;
        filmEntities = filmRepository.findAllByStatusAndFilmType_Code(CoreConstant.ACTIVE_STATUS, filmTypeCode, pageable);
        List<FilmDTO> filmDTOS = new ArrayList<FilmDTO>();
        for (FilmEntity entity : filmEntities) {
            FilmDTO filmDTO = filmConvert.toDTO(entity);
            filmDTOS.add(filmDTO);
        }
        return filmDTOS;
    }

    public List<FilmDTO> findByProperties(boolean trailer, int page, int limit, String sortExpression, String sortDirection) {
        Pageable pageable = pagingUtils.setPageable(page, limit, sortExpression, sortDirection);
        List<FilmEntity> filmEntities;
        filmEntities = filmRepository.findAllByStatusAndTrailerNotNull(CoreConstant.ACTIVE_STATUS, pageable);
        List<FilmDTO> filmDTOS = new ArrayList<FilmDTO>();
        for (FilmEntity entity : filmEntities) {
            FilmDTO filmDTO = filmConvert.toDTO(entity);
            filmDTOS.add(filmDTO);
        }
        return filmDTOS;
    }

    @Override
    public List<FilmDTO> findByProperties(String filmTypeCode, boolean trailer, int page, int limit, String sortExpression, String sortDirection) {
        Pageable pageable = pagingUtils.setPageable(page, limit, sortExpression, sortDirection);
        List<FilmEntity> filmEntities = filmRepository.findAllByFilmType_CodeAndTrailerNotNull(1, filmTypeCode, pageable);
        List<FilmDTO> filmDTOS = new ArrayList<FilmDTO>();
        for (FilmEntity entity : filmEntities) {
            FilmDTO filmDTO = filmConvert.toDTO(entity);
            filmDTOS.add(filmDTO);
        }
        return filmDTOS;
    }

    public List<FilmDTO> findByProperties(Long userId, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        List<FilmEntity> filmEntities;
        filmEntities = filmRepository.findAllByUserFollow(userId, CoreConstant.ACTIVE_STATUS, pageable);
        List<FilmDTO> filmDTOS = new ArrayList<FilmDTO>();
        for (FilmEntity entity : filmEntities) {
            FilmDTO filmDTO = filmConvert.toDTO(entity);
            filmDTOS.add(filmDTO);
        }
        return filmDTOS;
    }

    public int getTotalItem(String search, String filmType, String category, String country, String year) {
        Integer status = 1;
        return (int) filmRepository.countAllByProperties(search, filmType, category, country, year, status);
    }

    @Override
    public int getTotalFilmFavorite(Long userID, int likeORfollow) {
        return (int) filmRepository.findFilmFavoriteByUserId(userID, likeORfollow);
    }

    @Override
    public int getTotalItemByCategory(String categoryCode, Integer status) {
        return (int) filmRepository.countAllByCategory(categoryCode, status);
    }

    @Override
    public int getTotalItemByCountry(String country, Integer status) {
        return (int) filmRepository.countAllByCountry_CodeAndStatus(country, status);
    }

    @Override
    public int getTotalItemByFilmType(String filmType, Integer status) {
        return (int) filmRepository.countAllByFilmType_CodeAndStatus(filmType, status);
    }

    public int getTotalItem(String userName, String filmType, String category, String country, String year, String search) {
        if (!userName.equals("ADMIN")) {
            return (int) filmRepository.countAllByPosterAndProperties(userName, search, CoreConstant.ACTIVE_STATUS, filmType, category, country, year);
        } else {
            return (int) filmRepository.countAllByAdminAndProperties(search, search, filmType, category, country, year);
        }
    }

    public FilmDTO findOneById(Long id) {
        FilmEntity entity = filmRepository.getOne(id);
        Integer countEpisode = episodeRepository.countAllByFilm_IdAndStatus(entity.getId(), CoreConstant.ACTIVE_STATUS);
        if (entity == null) return null;
        FilmDTO filmDTO = filmConvert.toDTO(entity);
        filmDTO.setEpisodesCount(countEpisode);
        return filmDTO;
    }

    @Autowired
    private ActorConvert actorConvert;

    public FilmDTO findOne(Long id, String code, Integer status) {
        FilmEntity entity = filmRepository.findByIdAndCodeAndStatus(id, code, status);
        if (entity == null) {
            return null;
        }
        Integer countEpisode = episodeRepository.countAllByFilm_IdAndStatus(entity.getId(), CoreConstant.ACTIVE_STATUS);
        FilmDTO filmDTO = filmConvert.toDTO(entity);
        List<ActorDTO> actorDTOS = new ArrayList<>();
        for (ActorEntity actorEntity : entity.getActors()) {
            ActorDTO actorDTO = actorConvert.toDTO(actorEntity);
            actorDTOS.add(actorDTO);
        }
        filmDTO.setActors(actorDTOS);
        filmDTO.setEpisodesCount(countEpisode);
        return filmDTO;
    }

    public FilmDTO findOneById(Long id, Integer status) {
        FilmEntity entity = filmRepository.findByIdAndStatus(id, status);
        if (entity == null) {
            return null;
        }
        Integer countEpisode = episodeRepository.countAllByFilm_IdAndStatus(entity.getId(), CoreConstant.ACTIVE_STATUS);
        FilmDTO filmDTO = filmConvert.toDTO(entity);
        filmDTO.setEpisodesCount(countEpisode);
        return filmDTO;
    }

    public FilmDTO findOneById(Long id, String poster, Integer status) {
        FilmEntity entity = filmRepository.findByIdAndCreatedByAndStatus(id, poster, status);
        if (entity == null) {
            return null;
        }
        Integer countEpisode = episodeRepository.countAllByFilm_IdAndStatus(entity.getId(), CoreConstant.ACTIVE_STATUS);
        FilmDTO filmDTO = filmConvert.toDTO(entity);
        filmDTO.setEpisodesCount(countEpisode);
        return filmDTO;
    }

    public FilmDTO findOneByCode(String code) {
        FilmEntity entity = filmRepository.findByCode(code);
        if (entity == null) {
            return null;
        }
        Integer countEpisode = episodeRepository.countAllByFilm_IdAndStatus(entity.getId(), CoreConstant.ACTIVE_STATUS);
        FilmDTO filmDTO = filmConvert.toDTO(entity);
        filmDTO.setEpisodesCount(countEpisode);
        return filmDTO;
    }

    public boolean checkPosterFilm(Long id, String userName) {
        long count = filmRepository.countAllByIdAndEmployee_UserNameAndStatus(id, userName, 1);
        if (count >= 1) {
            return true;
        } else {
            return false;
        }
    }

    @Autowired
    private StringGlobalUtils stringGlobalUtils;

    @Transactional
    public FilmDTO save(FilmDTO filmDTO) throws Exception {
        FilmEntity entity;
        String code = stringGlobalUtils.covertToString(filmDTO.getName());
        if (filmDTO.getId() != null) {
            entity = filmRepository.getOne(filmDTO.getId());
            entity = filmConvert.toEntity(filmDTO, entity, "image", "image2", "trailerYoutube", "trailer");
            if (entity.getScores() == null) entity.setScores(0.0);
            if (entity.getView() == null) entity.setView(0);
        } else {
            entity = filmConvert.toEntity(filmDTO);
            entity.setScores(0.0);
            entity.setView(0);
        }
        entity.setCode(code);
        List<CategoryEntity> categoryEntities = new ArrayList<CategoryEntity>();
        if (filmDTO.getCategories() != null && filmDTO.getCategories().size() > 0) {
            for (CategoryDTO categoryDTO : filmDTO.getCategories()) {
                CategoryEntity categoryEntity = new CategoryEntity();
                BeanUtils.copyProperties(categoryDTO, categoryEntity);
                categoryEntities.add(categoryEntity);
            }
        } else {
            throw new Exception();
        }
        List<ActorEntity> actorEntities = new ArrayList<ActorEntity>();
        if (filmDTO.getActors() != null) {
            for (ActorDTO actorDTO : filmDTO.getActors()) {
                ActorEntity actorEntity = new ActorEntity();
                BeanUtils.copyProperties(actorDTO, actorEntity);
                actorEntities.add(actorEntity);
            }
        }
        entity.setCategories(categoryEntities);
        entity.setActors(actorEntities);
        entity = filmRepository.save(entity);
        filmDTO = filmConvert.toDTO(entity);
        return filmDTO;
    }

    @Transactional
    public boolean updateFilmStatus(FilmDTO filmDTO) {
        FilmEntity filmEntity = filmRepository.getOne(filmDTO.getId());
        filmEntity.setStatus(filmDTO.getStatus());
        filmRepository.save(filmEntity);
        return true;
    }

    @Autowired
    private IEpisodeService episodeService;

    @Autowired
    private ICommentService commentService;

    @Autowired
    private EvaluateRepository evaluateRepository;

    @Transactional
    public void deleteById(Long[] ids) throws Exception {
        for (Long id : ids) {
            FilmEntity entity = filmRepository.getOne(id);
            for (EpisodeEntity episodeEntity : entity.getEpisodes()) {
                Long[] episodeIds = {episodeEntity.getId()};
                episodeService.delete(episodeIds);
            }
            for (CommentEntity commentEntity : entity.getComments()) {
                Long[] commentIds = {commentEntity.getId()};
                commentService.deleteComment(commentIds);
            }
            for (EvaluateEntity evaluateEntity : entity.getEvaluates()) {
                evaluateEntity.setFilm(null);
                evaluateRepository.save(evaluateEntity);
            }
            filmRepository.deleteById(id);
        }
    }

    @Autowired
    private IDriveService driveService;

    @Transactional
    public void updatePhotoToDrive(Long id, FileItem photo) throws IOException {
        FilmEntity filmEntity = filmRepository.getOne(id);
        if (StringUtils.isNotBlank(filmEntity.getImage())) {
            try {
                driveService.deleteFileById(filmEntity.getImage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String photoName = "film_image_" + id;
        File file = driveService.createGoogleFile(CoreConstant.FILM_IMAGE_ADDRESS_ID, photo.getContentType(), photoName, photo.getInputStream());
        driveService.createPublicPermission(file.getId());
        filmEntity.setImage(file.getId());
        filmRepository.save(filmEntity);
    }

    @Override
    public void updateImg1(Long id, String photoName) throws IOException {
        FilmEntity filmEntity = filmRepository.getOne(id);
        filmEntity.setImage(photoName);
        filmRepository.save(filmEntity);
    }

    @Override
    public void updateImg2(Long id, String photoName) throws IOException {
        FilmEntity filmEntity = filmRepository.getOne(id);
        filmEntity.setImage2(photoName);
        filmRepository.save(filmEntity);
    }

    public void updateTrailer(Long id, MultipartFile trailer, String trailerYoutube) throws IOException {
        FilmEntity filmEntity = filmRepository.getOne(id);
        if (!trailer.getOriginalFilename().isEmpty()) {
            if (StringUtils.isNotBlank(filmEntity.getTrailer())) {
                try {
                    driveService.deleteFileById(filmEntity.getTrailer());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            String trailerName = "film_trailer_" + id;
            File file = driveService.createGoogleFile(CoreConstant.FILM_TRAILER_ADDRESS_ID, trailer.getContentType(), trailerName, trailer.getInputStream());
            driveService.createPublicPermission(file.getId());
            filmEntity.setTrailer(file.getId());
        }
        filmEntity.setTrailerYoutube(trailerYoutube);
        filmRepository.save(filmEntity);
    }

    /*public void updateScores(Long id) {
        FilmEntity filmEntity = filmRepository.getOne(id);
        Double total = 0.0;
        int n = 0;
        for (EvaluateEntity evaluateEntity : filmEntity.getEvaluates()) {
            if (evaluateEntity.getScores() != null) {
                total = total + evaluateEntity.getScores();
                n++;
            }
        }
        filmEntity.setScores(total / n);
        filmEntity.setTotalVote(n);
        filmRepository.save(filmEntity);
    }*/

    @Autowired
    private IEvaluateService evaluateService;

    public Double updateScores(Long id) {
        FilmEntity filmEntity = filmRepository.getOne(id);
        Double scores = Math.ceil(evaluateService.getAvgScores(id) * 10) / 10;
        Integer total = evaluateService.getTotalVote(id);
        filmEntity.setScores(scores);
        filmEntity.setTotalVote(total);
        return filmRepository.save(filmEntity).getScores();
    }

    public void updateView(Long id) {
        FilmEntity filmEntity = filmRepository.getOne(id);
        int view;
        if (filmEntity.getView() == null) {
            view = 1;
        } else {
            view = filmEntity.getView() + 1;
        }

        filmEntity.setView(view);
        filmRepository.save(filmEntity);
    }

    @Override
    public int getTotalItem() {
        return (int) filmRepository.count();
    }
}
