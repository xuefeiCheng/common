package com.ebupt.portal.canyon.system.service;

import com.ebupt.portal.canyon.system.entity.Menu;

import java.util.List;
import java.util.Set;

/**
 * 菜单管理业务层接口
 *
 * @author chy
 * @date 2019-03-10 16:49
 */
public interface MenuService {

	/**
	 * 根据menuId集合，获取对应类型的菜单集合
	 *
	 * @param menuIds
	 *                  menuId集合
	 * @param type
	 *                  菜单类型 1-菜单 2-按钮
	 * @return
	 *                  菜单信息集合
	 */
	Set<Menu> findByMenuIds(Set<String> menuIds, String type);

	/**
	 * 查询所有角色信息
	 *
	 * @return
	 *              角色信息集合
	 */
	List<Menu> findAll();
}
