package com.movie.core.service.impl;

import com.google.api.services.drive.model.File;
import com.movie.core.constant.CoreConstant;
import com.movie.core.dto.EpisodeDTO;
import com.movie.core.entity.EpisodeEntity;
import com.movie.core.repository.EpisodeRepository;
import com.movie.core.service.IEpisodeService;
import com.movie.core.convert.EpisodeConvert;
import com.movie.core.service.utils.PagingUtils;
import org.apache.commons.fileupload.FileItem;
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
public class EpisodeService implements IEpisodeService {
    @Autowired
    private EpisodeRepository episodeRepository;
    @Autowired
    private EpisodeConvert episodeConvert;
    @Autowired
    private DriveService driveService;
    @Autowired
    private PagingUtils pagingUtils;

    @Transactional
    public void updateVideo(Long id, FileItem video) throws IOException {
        EpisodeEntity episodeEntity = episodeRepository.getOne(id);
        String filmFolderName = episodeEntity.getFilm().getId().toString();
        File filmFolder = getFilmFolder(filmFolderName);
        deleteEpisodeInDrive(episodeEntity.getEpisodeId());
        String videoName = episodeEntity.getEpisodeCode().toString();
        File file = driveService.createGoogleFile(filmFolder.getId(), video.getContentType(), videoName, video.getInputStream());
        driveService.createPublicPermission(file.getId());
        episodeEntity.setEpisodeId(file.getId());
        episodeRepository.save(episodeEntity);
    }


    @Transactional
    public void updateVideo(Long id, MultipartFile video) throws IOException {
        EpisodeEntity episodeEntity = episodeRepository.getOne(id);
        String filmFolderName = episodeEntity.getFilm().getId().toString();
        File filmFolder = getFilmFolder(filmFolderName);
        deleteEpisodeInDrive(episodeEntity.getEpisodeId());
        String videoName = episodeEntity.getEpisodeCode().toString();
        File file = driveService.createGoogleFile(filmFolder.getId(), video.getContentType(), videoName, video.getInputStream());
        driveService.createPublicPermission(file.getId());
        episodeEntity.setEpisodeId(file.getId());
        episodeRepository.save(episodeEntity);
    }

    private void deleteEpisodeInDrive(String episodeId) {
        try {
            if (episodeId!=null && !episodeId.isEmpty()) {
                driveService.deleteFileById(episodeId);
            }
        } catch (IOException e) {
            System.out.println("Không có file để xóa trên drive");
        }
    }

    private File getFilmFolder(String filmFolderName) throws IOException {
        List<File> fileList = driveService.getSubFoldersByName(CoreConstant.VIDEO_ADDRESS_ID, filmFolderName);
        File filmFolder;
        if (fileList.size() < 1) {
            filmFolder = driveService.createNewFolder(CoreConstant.VIDEO_ADDRESS_ID, filmFolderName);
        } else {
            filmFolder = fileList.get(0);
        }
        return filmFolder;
    }

    public Long updateStatus(Long id, Integer status) {
        EpisodeEntity episodeEntity = episodeRepository.getOne(id);
        episodeEntity.setStatus(status);
        return episodeRepository.save(episodeEntity).getId();
    }

    @Transactional
    public EpisodeDTO save(EpisodeDTO episodeDTO) {
        EpisodeEntity episodeEntity = episodeConvert.toEntity(episodeDTO);
        Pageable pageable = PageRequest.of(0, 1, Sort.Direction.DESC, "episodeCode");
        List<EpisodeEntity> episodeEntities = episodeRepository.findByFilm_IdAndStatus(episodeDTO.getFilm().getId(), CoreConstant.ACTIVE_STATUS, pageable);
        Integer codePrev = 0;
        if (episodeEntities.size() > 0) {
            codePrev = episodeEntities.get(0).getEpisodeCode();
        }
        codePrev++;
        if (episodeDTO.getId() != null) {
            EpisodeEntity episodeOld = episodeRepository.getOne(episodeDTO.getId());
            episodeEntity.setEpisodeId(episodeOld.getEpisodeId());
            episodeEntity.setFilm(episodeOld.getFilm());
            if (episodeDTO.getVideo() == null) {
                episodeEntity.setEpisodeId(episodeOld.getEpisodeId());
            }
            if (episodeEntity.getEpisodeCode() == null) {
                episodeEntity.setEpisodeCode(episodeOld.getEpisodeCode());
            }
        } else {
            if (episodeDTO.getEpisodeCode() == null) {
                episodeEntity.setEpisodeCode(codePrev);
            }
        }
        episodeEntity = episodeRepository.save(episodeEntity);
        if (episodeDTO.getVideo() != null) {
            try {
                updateVideo(episodeEntity.getId(), episodeDTO.getVideo());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return episodeConvert.toDTO(episodeEntity);
    }

    public void delete(Long[] ids) {
        for (Long id : ids) {
            EpisodeEntity episodeEntity = episodeRepository.getOne(id);
            if (episodeEntity.getEpisodeId() != null) {
                try {
                    driveService.deleteFileById(episodeEntity.getEpisodeId());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            episodeRepository.deleteById(id);
        }
    }

    public EpisodeDTO findOneByFilmAndCode(String filmCode, Integer code) {
        EpisodeEntity episodeEntity = episodeRepository.findByFilm_CodeAndEpisodeCode(filmCode, code);
        if (episodeEntity != null) {
            return episodeConvert.toDTO(episodeEntity);
        }
        return null;
    }

    public EpisodeDTO findOneById(Long id) {
        EpisodeEntity episodeEntity = episodeRepository.getOne(id);
        if (episodeEntity != null) {
            return episodeConvert.toDTO(episodeEntity);
        }
        return null;
    }

    public EpisodeDTO findOneById(Long id, Integer status) {
        EpisodeEntity episodeEntity = episodeRepository.findByIdAndStatus(id, status);
        if (episodeEntity != null) {
            return episodeConvert.toDTO(episodeEntity);
        }
        return null;
    }

    public List<EpisodeDTO> findAllByFilmId(Long id, Integer status) {
        List<EpisodeEntity> episodeEntities = episodeRepository.findAllByStatusAndFilm_Id(status, id);
        List<EpisodeDTO> episodeDTOS = ConvertToDTOS(episodeEntities);
        return episodeDTOS;
    }

    public List<EpisodeDTO> findAllByFilmId(Long id) {
        List<EpisodeEntity> episodeEntities = episodeRepository.findAllByFilm_Id(id);
        List<EpisodeDTO> episodeDTOS = ConvertToDTOS(episodeEntities);
        return episodeDTOS;
    }

    @Override
    public List<EpisodeDTO> findAllByProperties(Long id, String search, boolean status, int page, int limit, String sortBy, String sortDrc) {
        search = search.toLowerCase();
        Pageable pageable = pagingUtils.setPageable(page, limit, sortBy, sortDrc);
        List<EpisodeEntity> episodeEntities;
        if (status) {
            if (id != null) {
                episodeEntities = episodeRepository.findByProperties(id, search, 1, pageable);
            } else {
                episodeEntities = episodeRepository.findByProperties(search, 1, pageable);
            }
        } else {
            if (id != null) {
                episodeEntities = episodeRepository.findByProperties(id, search, pageable);
            } else {
                episodeEntities = episodeRepository.findByProperties(search, pageable);
            }
        }
        List<EpisodeDTO> episodeDTOS = ConvertToDTOS(episodeEntities);
        return episodeDTOS;
    }

    @Override
    public int countAllByProperties(Long id, String search, boolean status) {
        search = search.toLowerCase();
        int result = 0;
        if (status) {
            if (id != null) {
                result = episodeRepository.countByProperties(id, search, 1);
            } else {
                result = episodeRepository.countByProperties(search, 1);
            }
        } else {
            if (id != null) {
                result = episodeRepository.countByProperties(id, search);
            } else {
                result = episodeRepository.countByProperties(search);
            }
        }
        return result;
    }

    private List<EpisodeDTO> ConvertToDTOS(List<EpisodeEntity> episodeEntities) {
        List<EpisodeDTO> episodeDTOS = new ArrayList<EpisodeDTO>();
        for (EpisodeEntity entity : episodeEntities) {
            EpisodeDTO episodeDTO = episodeConvert.toDTO(entity);
            episodeDTOS.add(episodeDTO);
        }
        return episodeDTOS;
    }

    public boolean checkEmployeeCreate(String username, Long id) {
        Integer count = episodeRepository.countByFilm_Employee_UserNameAndId(username, id);
        if (count >= 1) {
            return true;
        }
        return false;
    }
}
