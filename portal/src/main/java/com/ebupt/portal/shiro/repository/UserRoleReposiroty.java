package com.ebupt.portal.shiro.repository;

import com.ebupt.portal.shiro.entity.UserRoleEntity;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface UserRoleReposiroty extends JpaRepository<UserRoleEntity, Integer> {

    @Query("select roleId from UserRoleEntity where userName = ?1")
    Set<String> findRoleIdByUserName(String userName);

}
