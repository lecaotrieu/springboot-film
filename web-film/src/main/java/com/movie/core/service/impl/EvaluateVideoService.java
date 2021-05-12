package com.movie.core.service.impl;

import com.movie.core.convert.EvaluateVideoConvert;
import com.movie.core.dto.EvaluateVideoDTO;
import com.movie.core.entity.EvaluateVideoEntity;
import com.movie.core.repository.EvaluateVideoRepository;
import com.movie.core.service.IEvaluateVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EvaluateVideoService implements IEvaluateVideoService {
    @Autowired
    private EvaluateVideoRepository evaluateVideoRepository;
    @Autowired
    private EvaluateVideoConvert evaluateVideoConvert;

    @Transactional
    @Override
    public Integer save(EvaluateVideoDTO evaluateVideoDTO) throws Exception {
        EvaluateVideoEntity evaluateVideoEntity = evaluateVideoRepository.findByVideo_IdAndUser_Id(evaluateVideoDTO.getVideo().getId(), evaluateVideoDTO.getUser().getId());
        if (evaluateVideoEntity == null) {
            evaluateVideoEntity = evaluateVideoConvert.toEntity(evaluateVideoDTO);
        } else {
            evaluateVideoEntity.setLiked(evaluateVideoDTO.getLiked());
        }
        return evaluateVideoRepository.save(evaluateVideoEntity).getLiked();
    }

    @Override
    public EvaluateVideoDTO findByVideoAndUser(Long videoId, Long userId) {
        EvaluateVideoEntity evaluateVideoEntity = evaluateVideoRepository.findByVideo_IdAndUser_Id(videoId, userId);
        if (evaluateVideoEntity != null) {
            return evaluateVideoConvert.toDTO(evaluateVideoEntity);
        }
        return null;
    }
}
