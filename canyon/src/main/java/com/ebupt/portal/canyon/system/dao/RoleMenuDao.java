package com.ebupt.portal.canyon.system.dao;

import com.ebupt.portal.canyon.system.entity.Menu;
import com.ebupt.portal.canyon.system.entity.RoleMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * 角色权限管理持久层
 *
 * @author chy
 * @date 2019-03-08 17:02
 */
public interface RoleMenuDao extends JpaRepository<RoleMenu, Long> {

	/**
	 * 根据角色ID获取对应的权限集合
	 *
	 * @param role
	 *              角色ID
	 * @return
	 *              权限集合
	 */
	@Query("select rm.menuId from RoleMenu rm where rm.roleId = :role")
	Set<String> findByRoleId(@Param("role") String role);

	/**
	 * 根据角色ID删除角色权限关联信息
	 *
	 * @param roleId
	 *                  角色ID
	 */
	@Modifying
	@Query("delete from RoleMenu rm where rm.roleId = :roleId")
	void deleteByRoleId(@Param("roleId") String roleId);

	/**
	 * 根据角色ID和权限ID集合删除对应关系
	 *
	 * @param roleId
	 *                      角色ID
	 * @param removeMenus
	 *                      权限ID集合
	 */
	@Modifying
	@Query("delete from RoleMenu rm where rm.roleId = :roleId and rm.menuId in (:menuIds)")
	void deleteByRoleIdAndMenuId(@Param("roleId") String roleId, @Param("menuIds") List<String> removeMenus);
}
