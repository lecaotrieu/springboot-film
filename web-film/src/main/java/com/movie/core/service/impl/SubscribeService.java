package com.movie.core.service.impl;

import com.movie.core.convert.SubscribeConvert;
import com.movie.core.dto.SubscribeDTO;
import com.movie.core.entity.SubscribeEntity;
import com.movie.core.repository.SubscribeRepository;
import com.movie.core.service.ISubscribeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SubscribeService implements ISubscribeService {
    @Autowired
    private SubscribeRepository subscribeRepository;
    @Autowired
    private SubscribeConvert subscribeConvert;

    @Transactional
    @Override
    public Integer save(SubscribeDTO subscribeDTO) throws Exception {
        SubscribeEntity subscribeEntity = subscribeRepository.findByUserFollow_IdAndUser_Id(subscribeDTO.getUserFollow().getId(), subscribeDTO.getUser().getId());
        if (subscribeEntity == null) {
            subscribeEntity = subscribeConvert.toEntity(subscribeDTO);
        } else {
            subscribeEntity.setFollow(subscribeDTO.getFollow());
        }
        return subscribeRepository.save(subscribeEntity).getFollow();
    }

    @Override
    public SubscribeDTO findByUserFollowAndUser(Long userFollowId, Long userId) {
        SubscribeEntity subscribeEntity = subscribeRepository.findByUserFollow_IdAndUser_Id(userFollowId, userId);
        if (subscribeEntity != null) {
            return subscribeConvert.toDTO(subscribeEntity);
        }
        return null;
    }

}
