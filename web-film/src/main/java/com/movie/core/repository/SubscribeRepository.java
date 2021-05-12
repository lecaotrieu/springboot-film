package com.movie.core.repository;

import com.movie.core.entity.SubscribeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscribeRepository extends JpaRepository<SubscribeEntity, Long> {
    SubscribeEntity findByUserFollow_IdAndUser_Id(Long userId, Long userFollowId);

    int countAllByFollowIsLikeAndUserFollow_Id(Integer follow, Long userFollowId);
}
