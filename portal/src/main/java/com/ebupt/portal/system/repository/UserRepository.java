package com.ebupt.portal.system.repository;

import com.ebupt.portal.system.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    UserEntity findByUserName(String userName);

    @Transactional
    @Modifying
    @Query("update UserEntity set loginTime = ?1, state = '1' where userName = ?2")
    void updateTimeByUserName(String time, String username);
}
