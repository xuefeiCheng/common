package com.ebupt.portal.canyon.system.controller;

import com.ebupt.portal.canyon.common.annotation.EBLog;
import com.ebupt.portal.canyon.common.dto.JsonResult;
import com.ebupt.portal.canyon.common.enums.UpdateEnum;
import com.ebupt.portal.canyon.common.util.JsonResultUtil;
import com.ebupt.portal.canyon.common.vo.PageVo;
import com.ebupt.portal.canyon.system.entity.Menu;
import com.ebupt.portal.canyon.system.entity.Role;
import com.ebupt.portal.canyon.system.service.RoleMenuService;
import com.ebupt.portal.canyon.system.service.RoleService;
import com.ebupt.portal.canyon.system.vo.RoleAuthVo;
import com.ebupt.portal.canyon.system.vo.RoleSearchVo;
import com.ebupt.portal.canyon.system.vo.RoleVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 角色管理控制层
 *
 * @author chy
 * @date 2019-03-22 15:12
 */
@Slf4j
@RestController
@RequestMapping("/role")
@Api(description = "角色管理")
public class RoleController {

	private final RoleService roleService;
	private final RoleMenuService roleMenuService;

	@Autowired
	public RoleController(RoleService roleService, RoleMenuService roleMenuService) {
		this.roleService = roleService;
		this.roleMenuService = roleMenuService;
	}

	@GetMapping
	@ApiOperation("获取所有角色信息")
	public JsonResult list() {
		List<Role> list = this.roleService.findAll();
		return JsonResult.ok(list);
	}

	@PostMapping
	@ApiOperation("分页查询角色信息")
	public JsonResult findByPage(@Valid @RequestBody PageVo<RoleSearchVo> roleSearchVo, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			String errMsg = bindingResult.getFieldErrors().get(0).getDefaultMessage();
			return JsonResult.paramError(errMsg);
		}
		Page<Role> page = this.roleService.findByPage(roleSearchVo);
		return JsonResultUtil.convert(page);
	}

	@PostMapping("/add")
	@ApiOperation("新增角色")
	@EBLog("新增角色{roleVo.roleName}")
	public JsonResult add(@Valid @RequestBody RoleVo roleVo, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			String errMsg = bindingResult.getFieldErrors().get(0).getDefaultMessage();
			return JsonResult.paramError(errMsg);
		}
		UpdateEnum result = this.roleService.save(roleVo);
		return JsonResultUtil.convert(result);
	}

	@DeleteMapping("/{roleId}")
	@ApiOperation("删除角色")
	@EBLog("删除角色{roleId}")
	public JsonResult delete(@PathVariable("roleId") String roleId) {
		UpdateEnum result = this.roleService.deleteByRoleId(roleId);
		return JsonResultUtil.convert(result);
	}

	@GetMapping("/auth/{roleId}")
	@ApiOperation("查询角色拥有的权限")
	public JsonResult auth(@PathVariable("roleId") String roleId) {
		Set<String> roleIds = new HashSet<>();
		roleIds.add(roleId);
		Set<String> menus = this.roleMenuService.findByRoles(roleIds);
		return JsonResult.ok(menus);
	}

	@PostMapping("/auth")
	@ApiOperation("更新角色拥有的权限")
	@EBLog("为角色{roleAuthVo.roleId}新增权限{roleAuthVo.addNodes}删除权限{roleAuthVo.removeNodes}")
	public JsonResult updateAuth(@Valid @RequestBody RoleAuthVo roleAuthVo, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			String errMsg = bindingResult.getFieldErrors().get(0).getDefaultMessage();
			return JsonResult.paramError(errMsg);
		}

		UpdateEnum result = this.roleMenuService.updateRoleAuth(roleAuthVo);
		return JsonResultUtil.convert(result);
	}

}
