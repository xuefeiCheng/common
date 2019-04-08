package com.ebupt.portal.shiro.repository;

import com.ebupt.portal.shiro.entity.RoleMenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface RoleMenuRepository extends JpaRepository<RoleMenuEntity, Integer> {

    @Query("select menuId from RoleMenuEntity where roleId = ?1")
    Set<String> findMenuIdByRoleId(String roleId);

}
