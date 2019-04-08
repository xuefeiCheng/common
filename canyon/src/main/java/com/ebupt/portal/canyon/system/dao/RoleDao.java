package com.ebupt.portal.canyon.system.dao;

import com.ebupt.portal.canyon.system.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 角色管理持久层
 *
 * @author chy
 * @date 2019-03-22 15:14
 */
public interface RoleDao extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {

	/**
	 * 根据角色ID删除角色ID
	 *
	 * @param roleId
	 *                  角色ID
	 */
	@Modifying
	@Query("delete from Role r where r.roleId = :roleId")
	void deleteByRoleId(@Param("roleId") String roleId);
}
