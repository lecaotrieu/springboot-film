package com.movie.core.repository;

import com.movie.core.entity.EvaluateVideoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluateVideoRepository extends JpaRepository<EvaluateVideoEntity, Long> {
    EvaluateVideoEntity findByVideo_IdAndUser_Id(Long videoId, Long userId);

    int countAllByLikedIsLikeAndVideo_Id(Integer like, Long videoId);
}
