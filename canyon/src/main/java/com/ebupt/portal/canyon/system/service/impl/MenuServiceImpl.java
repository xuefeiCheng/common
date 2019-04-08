package com.ebupt.portal.canyon.system.service.impl;

import com.ebupt.portal.canyon.common.util.SystemConstant;
import com.ebupt.portal.canyon.system.dao.MenuDao;
import com.ebupt.portal.canyon.system.entity.Menu;
import com.ebupt.portal.canyon.system.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 菜单管理业务层实现
 *
 * @author chy
 * @date 2019-03-10 16:49
 */
@Slf4j
@Service("menuService")
public class MenuServiceImpl implements MenuService {

	private final MenuDao menuDao;

	@Autowired
	public MenuServiceImpl(MenuDao menuDao) {
		this.menuDao = menuDao;
	}

	@Override
	public Set<Menu> findByMenuIds(Set<String> menuIds, String type) {
		if (SystemConstant.MENU_TYPE.equals(type) || SystemConstant.BTN_TYPE.equals(type)) {
			if (menuIds != null && menuIds.size() > 0) {
				return this.menuDao.findbyMenuIds(menuIds, type);
			} else {
				log.warn("menuIds为空");
			}
		} else {
			log.warn("查询非法的菜单类型: {}", type);
		}
		return null;
	}

	@Override
	public List<Menu> findAll() {
		return this.menuDao.findAll();
	}
}
