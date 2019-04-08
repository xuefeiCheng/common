package com.ebupt.portal.canyon.system.dao;

import com.ebupt.portal.canyon.system.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

/**
 * 用户角色管理持久层
 *
 * @author chy
 * @date 2019-03-08 16:58
 */
public interface UserRoleDao extends JpaRepository<UserRole, Long> {

	/**
	 * 根据用户查询角色ID集合
	 *
	 * @param userName
	 *                  用户名
	 * @return
	 *                  角色ID集合
	 */
	@Query("select ur.roleId from UserRole ur where ur.userName = :userName")
	Set<String> findByUserName(@Param("userName") String userName);

	/**
	 * 根据用户名和角色ID删除关联信息
	 *
	 * @param userName
	 *                  用户名
	 * @param roles
	 *                  角色ID集合
	 */
	@Modifying
	@Query("delete from UserRole ur where ur.userName = :userName and ur.roleId in (:roles)")
	void delete(@Param("userName") String userName, @Param("roles") Set<String> roles);

	/**
	 * 根据角色ID删除用户角色关联信息
	 *
	 * @param roleId
	 *                  角色ID
	 */
	@Modifying
	@Query("delete from UserRole ur where ur.roleId = :roleId")
	void deleteByRoleId(@Param("roleId") String roleId);
}
