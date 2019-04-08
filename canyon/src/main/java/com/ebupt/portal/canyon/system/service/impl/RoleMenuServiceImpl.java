package com.ebupt.portal.canyon.system.service.impl;

import com.ebupt.portal.canyon.common.enums.UpdateEnum;
import com.ebupt.portal.canyon.system.dao.RoleMenuDao;
import com.ebupt.portal.canyon.system.entity.Menu;
import com.ebupt.portal.canyon.system.entity.RoleMenu;
import com.ebupt.portal.canyon.system.service.RoleMenuService;
import com.ebupt.portal.canyon.system.vo.RoleAuthVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 角色权限管理业务层实现
 *
 * @author chy
 * @date 2019-03-08 17:01
 */
@Service("roleMenuService")
public class RoleMenuServiceImpl implements RoleMenuService {

	private final RoleMenuDao roleMenuDao;

	@Autowired
	public RoleMenuServiceImpl(RoleMenuDao roleMenuDao) {
		this.roleMenuDao = roleMenuDao;
	}

	@Override
	public Set<String> findByRoles(Set<String> roles) {
		Set<String> permissions = new HashSet<>();

		if (roles != null) {
			for (String role : roles) {
				permissions.addAll(this.roleMenuDao.findByRoleId(role));
			}
		}

		return permissions;
	}

	@Override
	public UpdateEnum deleteByRoleId(String roleId) {
		this.roleMenuDao.deleteByRoleId(roleId);
		return UpdateEnum.SUCCESS;
	}

	@Override
	public UpdateEnum updateRoleAuth(RoleAuthVo roleAuthVo) {
		List<String> addMenus = roleAuthVo.getAddNodes();
		List<String> removeMenus = roleAuthVo.getRemoveNodes();

		if (addMenus != null && addMenus.size() > 0) {
			for (String addMenuId: addMenus) {
				this.roleMenuDao.save(new RoleMenu(roleAuthVo.getRoleId(), addMenuId));
			}
		}

		if (removeMenus != null && removeMenus.size() > 0) {
			this.roleMenuDao.deleteByRoleIdAndMenuId(roleAuthVo.getRoleId(), removeMenus);
		}

		return UpdateEnum.SUCCESS;
	}

}
