package com.movie.core.repository;

import com.movie.core.entity.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query("select u from UserEntity u where lower(u.userName) like %?1%")
    List<UserEntity> findAllByUserName(String userName, Pageable pageble);

    @Query("select count(distinct u.userName) from UserEntity u where  lower(u.userName) like %?1%")
    long countAllByUserName(String search);

    UserEntity findByUserNameAndStatus(String userName, Integer status);

    UserEntity findAllById(Long id);

    UserEntity findAllByIdAndStatus(Long id, Integer status);

    @Query("select distinct f from UserEntity f where f.id = ?1")
    List<UserEntity> findAllByIds(Long id);

    @Query("select distinct u from UserEntity u join u.usersFollowed x where x.id = ?1")
    List<UserEntity> findAllMyFollow(Long id);

    @Query("select distinct u from UserEntity u join u.followers x where x.id = ?1")
    List<UserEntity> findAllFollower(Long id);

    @Query("select distinct u from UserEntity u join u.subscribes s where s.userFollow.id = ?1 and s.follow=?2")
    List<UserEntity> findAllUserFollowMe(Long userId, Integer follow);

    @Query("select distinct u from UserEntity u join u.beSubscribes s where s.user.id = ?1 and s.follow=?2")
    List<UserEntity> findAllUserFollowed(Long id, Integer follow);
}
