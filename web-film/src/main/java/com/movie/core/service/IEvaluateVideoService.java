package com.movie.core.service;

import com.movie.core.dto.EvaluateVideoDTO;

public interface IEvaluateVideoService {
    Integer save(EvaluateVideoDTO evaluateVideoDTO) throws Exception;

    EvaluateVideoDTO findByVideoAndUser(Long videoId, Long userId);
}
