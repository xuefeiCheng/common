package com.ebupt.portal.canyon.system.controller;

import com.ebupt.portal.canyon.common.dto.JsonResult;
import com.ebupt.portal.canyon.system.entity.Menu;
import com.ebupt.portal.canyon.system.service.MenuService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 权限管理控制层
 *
 * @author chy
 * @date 2019-03-24 15:03
 */
@Slf4j
@RestController
@RequestMapping("/menu")
@Api(description = "权限管理")
public class MenuController {

	private final MenuService menuService;

	@Autowired
	public MenuController(MenuService menuService) {
		this.menuService = menuService;
	}

	@GetMapping
	public JsonResult findAll() {
		List<Menu> list = this.menuService.findAll();
		return JsonResult.ok(list);
	}
}
