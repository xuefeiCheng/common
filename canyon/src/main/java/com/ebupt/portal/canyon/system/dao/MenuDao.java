package com.ebupt.portal.canyon.system.dao;

import com.ebupt.portal.canyon.system.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

/**
 * 菜单管理持久层
 *
 * @author chy
 * @date 2019-03-10 16:55
 */
public interface MenuDao extends JpaRepository<Menu, Long> {

	/**
	 * 根据menuId集合获取指定类型的菜单信息
	 *
	 * @param menuIds
	 *                  menuId集合
	 * @param type
	 *                  类型
	 * @return
	 *                  指定类型的菜单信息集合
	 */
	@Query("select m from Menu m where m.menuType = :type and m.menuId in (:menuIds)")
	Set<Menu> findbyMenuIds(@Param("menuIds") Set<String> menuIds, @Param("type") String type);
}
