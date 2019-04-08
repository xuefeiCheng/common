package com.ebupt.portal.canyon.system.service.impl;

import com.ebupt.portal.canyon.common.enums.UpdateEnum;
import com.ebupt.portal.canyon.common.util.SystemConstant;
import com.ebupt.portal.canyon.system.dao.UserRoleDao;
import com.ebupt.portal.canyon.system.entity.UserRole;
import com.ebupt.portal.canyon.system.service.UserRoleService;
import com.ebupt.portal.canyon.system.vo.UserRoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * 用户角色管理业务层实现
 *
 * @author chy
 * @date 2019-03-08 16:57
 */
@Service("userRoleService")
public class UserRoleServiceImpl implements UserRoleService {

	private final UserRoleDao userRoleDao;

	@Autowired
	public UserRoleServiceImpl(UserRoleDao userRoleDao) {
		this.userRoleDao = userRoleDao;
	}

	@Override
	public Set<String> findByUserName(String userName) {
		return this.userRoleDao.findByUserName(userName);
	}

	@Override
	public UpdateEnum updateUserRoles(UserRoleVo userRoleVo) {
		String userName = userRoleVo.getUserName();
		Set<String> roles = userRoleVo.getRoleIds();
		if (SystemConstant.ADD.equals(userRoleVo.getOp())) {
			for (String roleId: roles) {
				UserRole userRole = new UserRole(userName, roleId);
				this.userRoleDao.save(userRole);
			}
		} else {
			this.userRoleDao.delete(userName, roles);
		}
		return UpdateEnum.SUCCESS;
	}

	@Override
	public UpdateEnum deleteByRoleId(String roleId) {
		this.userRoleDao.deleteByRoleId(roleId);
		return UpdateEnum.SUCCESS;
	}
}
