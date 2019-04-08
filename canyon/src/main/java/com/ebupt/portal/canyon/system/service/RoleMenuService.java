package com.ebupt.portal.canyon.system.service;

import com.ebupt.portal.canyon.common.enums.UpdateEnum;
import com.ebupt.portal.canyon.system.vo.RoleAuthVo;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * 角色权限管理业务层接口
 *
 * @author chy
 * @date 2019-03-08 17:01
 */
public interface RoleMenuService {

	/**
	 * 根据角色集合获取对应的权限集合
	 *
	 * @param roles
	 *              角色集合
	 * @return
	 *              权限集合
	 */
	Set<String> findByRoles(Set<String> roles);

	/**
	 * 根据角色ID删除角色权限关联信息
	 *
	 * @param roleId
	 *                  角色ID
	 * @return
	 *                  操作结果
	 */
	@Transactional(rollbackFor = Exception.class)
	UpdateEnum deleteByRoleId(String roleId);

	/**
	 * 更新角色拥有的权限信息
	 *
	 * @param roleAuthVo
	 *                  角色权限更新信息
	 * @return
	 *                  操作结果
	 */
	@Transactional(rollbackFor = Exception.class)
	UpdateEnum updateRoleAuth(RoleAuthVo roleAuthVo);
}
