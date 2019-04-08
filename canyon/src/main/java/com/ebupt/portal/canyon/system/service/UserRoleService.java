package com.ebupt.portal.canyon.system.service;

import com.ebupt.portal.canyon.common.enums.UpdateEnum;
import com.ebupt.portal.canyon.system.vo.UserRoleVo;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * 用户角色管理业务层接口
 *
 * @author chy
 * @date 2019-03-08 16:56
 */
public interface UserRoleService {

	/**
	 * 根据用户名获取角色集合
	 *
	 * @param userName
	 *                  用户名
	 * @return
	 *                  角色集合
	 */
	Set<String> findByUserName(String userName);

	/**
	 * 修改用户角色关联信息
	 *
	 * @param userRoleVo
	 *                      用户角色关联信息
	 * @return
	 *                      操作结果
	 */
	@Transactional(rollbackFor = Exception.class)
	UpdateEnum updateUserRoles(UserRoleVo userRoleVo);

	/**
	 * 根据角色Id删除用户角色关联信息
	 *
	 * @param roleId
	 *                  角色ID
	 * @return
	 *                  操作结果
	 */
	@Transactional(rollbackFor = Exception.class)
	UpdateEnum deleteByRoleId(String roleId);
}
