package com.ebupt.portal.system.repository;

import com.ebupt.portal.system.entity.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuReposiroty extends JpaRepository<MenuEntity, Integer> {

    MenuEntity findByMenuId(String menuId);

}
