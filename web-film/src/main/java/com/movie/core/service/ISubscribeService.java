package com.movie.core.service;

import com.movie.core.dto.SubscribeDTO;

public interface ISubscribeService {
    Integer save(SubscribeDTO subscribeDTO) throws Exception;

    SubscribeDTO findByUserFollowAndUser(Long userFollowId, Long userId);
}
