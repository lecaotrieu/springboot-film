package com.movie.core.service.impl;

import com.google.api.services.drive.model.File;
import com.movie.core.constant.CoreConstant;
import com.movie.core.convert.VideoConvert;
import com.movie.core.dto.VideoDTO;
import com.movie.core.entity.FilmEntity;
import com.movie.core.entity.VideoEntity;
import com.movie.core.repository.EvaluateVideoRepository;
import com.movie.core.repository.VideoRepository;
import com.movie.core.service.ICommentVideoService;
import com.movie.core.service.IDriveService;
import com.movie.core.service.IVideoService;
import com.movie.core.service.utils.PagingUtils;
import com.movie.core.service.utils.StringGlobalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class VideoService implements IVideoService {

    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private StringGlobalUtils stringGlobalUtils;

    @Transactional
    @Override
    public VideoDTO save(VideoDTO videoDTO) throws Exception {
        VideoEntity videoEntity;
        String code = stringGlobalUtils.covertToString(videoDTO.getName());
        if (videoDTO.getId() != null) {
            videoEntity = videoRepository.getOne(videoDTO.getId());
            videoEntity = videoConvert.toEntity(videoDTO, videoEntity);
        } else {
            videoEntity = videoConvert.toEntity(videoDTO);
        }
        if (videoEntity.getView() == null) {
            videoEntity.setView(0);
            videoEntity.setTotalLike(0);
        }
        videoEntity.setCode(code);
        videoEntity = videoRepository.save(videoEntity);
        return videoConvert.toDTO(videoEntity);
    }

    @Transactional
    @Override
    public boolean updateVideoStatus(VideoDTO videoDTO) {
        VideoEntity videoEntity = videoRepository.getOne(videoDTO.getId());
        videoEntity.setStatus(videoDTO.getStatus());
        videoRepository.save(videoEntity);
        return true;
    }

    @Autowired
    private ICommentVideoService commentVideoService;

    @Override
    public void deleteById(Long[] ids) throws Exception {
        for (Long id : ids) {
            videoRepository.deleteById(id);
        }
    }

    @Autowired
    private PagingUtils pagingUtils;

    @Autowired
    private VideoConvert videoConvert;

    @Override
    public List<VideoDTO> findByProperties(String search, String userName, Integer status, int page, int limit, String sortExpression, String sortDirection) {
        search = search.toLowerCase();
        Pageable pageable = pagingUtils.setPageable(page, limit, sortExpression, sortDirection);
        List<VideoEntity> videoEntities;
        if (userName == null || userName.isEmpty()) {
            if (status != null) {
                videoEntities = videoRepository.findAllByProperties(search, status, pageable);

            } else {
                videoEntities = videoRepository.findAllByProperties(search, pageable);
            }
        } else {
            if (status != null) {
                videoEntities = videoRepository.findAllByProperties(search, userName, status, pageable);
            } else {
                videoEntities = videoRepository.findAllByProperties(search, userName, pageable);
            }
        }
        List<VideoDTO> videoDTOS = new ArrayList<>();
        for (VideoEntity entity : videoEntities) {
            if (status == null) {
                VideoDTO videoDTO = videoConvert.toDTO(entity);
                videoDTOS.add(videoDTO);
            } else if (entity.getStatus().equals(status)) {
                VideoDTO videoDTO = videoConvert.toDTO(entity);
                videoDTOS.add(videoDTO);
            }
        }
        return videoDTOS;
    }

    @Override
    public List<VideoDTO> findAll() {
        List<VideoEntity> videoEntities;
        videoEntities = videoRepository.findAllByStatus(CoreConstant.ACTIVE_STATUS);
        List<VideoDTO> videoDTOS = new ArrayList<>();
        for (VideoEntity entity : videoEntities) {
            VideoDTO videoDTO = videoConvert.toDTO(entity);
            videoDTOS.add(videoDTO);
        }
        return videoDTOS;
    }


    @Autowired
    private IDriveService driveService;

    @Override
    public String uploadVideoToDrive(Long videoId, MultipartFile video) throws IOException {
        if (!video.getOriginalFilename().isEmpty()) {
            String videoName = "user_video_" + videoId;
            File file = driveService.createGoogleFile(CoreConstant.VIDEO_USER_ADDRESS_ID, video.getContentType(), videoName, video.getInputStream());
            driveService.createPublicPermission(file.getId());
            return file.getId();
        }
        return null;
    }

    @Override
    public int getTotalItem(String search, String userName) {
        if (userName == null || userName.isEmpty()) {
            return (int) videoRepository.countAllByProperties(search);
        } else {
            return (int) videoRepository.countAllByProperties(search, userName);
        }
    }

    @Override
    public VideoDTO findOneById(Long id) {
        VideoEntity videoEntity = videoRepository.getOne(id);
        return videoConvert.toDTO(videoEntity);
    }

    @Override
    public VideoDTO findOneByIdAndStatus(Long id, Integer status) {
        VideoEntity videoEntity = videoRepository.findByIdAndStatus(id, status);
        return videoConvert.toDTO(videoEntity);
    }

    @Override
    public VideoDTO findOneById(Long id, Long userId) {
        VideoEntity videoEntity = videoRepository.findByIdAndUser_Id(id, userId);
        return videoConvert.toDTO(videoEntity);
    }

    @Override
    public boolean checkUser(Long userId, Long videoId) {
        VideoEntity videoEntity = videoRepository.getOne(videoId);
        if (userId.equals(videoEntity.getUser().getId())) {
            return true;
        }
        return false;
    }

    public List<VideoDTO> findByProperties(Long userId) {
        List<VideoEntity> videoEntities;
        videoEntities = videoRepository.findAllByUser_Id(userId);
        List<VideoDTO> videoDTOS = new ArrayList<>();
        for (VideoEntity entity : videoEntities) {
            VideoDTO videoDTO = videoConvert.toDTO(entity);
            videoDTOS.add(videoDTO);
        }
        return videoDTOS;
    }

    @Override
    public List<VideoDTO> findByProperties(Long userId, Integer status) {
        List<VideoEntity> videoEntities;
        videoEntities = videoRepository.findAllByUser_IdAndStatus(userId, status);
        List<VideoDTO> videoDTOS = new ArrayList<>();
        for (VideoEntity entity : videoEntities) {
            VideoDTO videoDTO = videoConvert.toDTO(entity);
            videoDTOS.add(videoDTO);
        }
        return videoDTOS;
    }


    @Override
    public List<VideoDTO> findByProperties(String search, Integer status, int page, int limit, String sortExpression, String sortDirection) {
        Pageable pageable = pagingUtils.setPageable(page, limit, sortExpression, sortDirection);
        List<VideoEntity> videoEntities = videoRepository.findAllBySearchAndStatus(search, status, pageable);
        List<VideoDTO> videoDTOS = new ArrayList<>();
        for (VideoEntity entity : videoEntities) {
            VideoDTO videoDTO = videoConvert.toDTO(entity);
            videoDTOS.add(videoDTO);
        }
        return videoDTOS;
    }

    @Override
    public List<VideoDTO> findAllToViewInHomePage(Integer status, int page, int limit) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate").descending().and(Sort.by("view").descending());
        Pageable pageable = PageRequest.of(page - 1, limit, sort);
        List<VideoEntity> videoEntities = videoRepository.findAllBySearchAndStatus("", status, pageable);
        List<VideoDTO> videoDTOS = new ArrayList<>();
        for (VideoEntity entity : videoEntities) {
            VideoDTO videoDTO = videoConvert.toDTO(entity);
            videoDTOS.add(videoDTO);
        }
        return videoDTOS;
    }

    @Override
    public int getTotalItem(String search, Integer status) {
        return (int) videoRepository.countAllBySearchAndStatus(search, status);
    }

    @Autowired
    private EvaluateVideoRepository evaluateVideoRepository;

    @Transactional
    @Override
    public Integer setTotalLike(Long videoId) {
        Integer totalLike = evaluateVideoRepository.countAllByLikedIsLikeAndVideo_Id(1, videoId);
        VideoEntity videoEntity = videoRepository.getOne(videoId);
        videoEntity.setTotalLike(totalLike);
        videoRepository.save(videoEntity);
        return totalLike;
    }

    @Override
    public void updateView(Long videoId) {
        VideoEntity videoEntity = videoRepository.getOne(videoId);
        int view;
        if (videoEntity.getView() == null) {
            view = 1;
        } else {
            view = videoEntity.getView() + 1;
        }

        videoEntity.setView(view);
        videoRepository.save(videoEntity);
    }

    @Override
    public int getTotalItem(String search, String userName, Integer status) {
        if (userName == null || userName.isEmpty()) {
            if (status != null) {
                return (int) videoRepository.countAllByProperties(search, status);
            }
            return (int) videoRepository.countAllByProperties(search);
        } else {
            if (status != null) {
                return (int) videoRepository.countAllByProperties(search, userName, status);
            }
            return (int) videoRepository.countAllByProperties(search, userName);
        }
    }
}
