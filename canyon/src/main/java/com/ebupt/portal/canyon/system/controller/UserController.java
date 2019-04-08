package com.ebupt.portal.canyon.system.controller;

import com.ebupt.portal.canyon.common.annotation.EBLog;
import com.ebupt.portal.canyon.common.dto.JsonResult;
import com.ebupt.portal.canyon.common.enums.UpdateEnum;
import com.ebupt.portal.canyon.common.util.JsonResultUtil;
import com.ebupt.portal.canyon.common.vo.PageVo;
import com.ebupt.portal.canyon.system.entity.User;
import com.ebupt.portal.canyon.system.service.UserRoleService;
import com.ebupt.portal.canyon.system.service.UserService;
import com.ebupt.portal.canyon.system.verify.AddUserVerify;
import com.ebupt.portal.canyon.system.verify.UpdateUserVerify;
import com.ebupt.portal.canyon.system.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

/**
 * 用户管理控制层
 *
 * @author chy
 * @date 2019-03-19 17:51
 */
@RestController
@RequestMapping("/user")
@Api(description = "用户管理")
public class UserController {

	private final UserService userService;
	private final UserRoleService userRoleService;

	@Autowired
	public UserController(UserService userService, UserRoleService userRoleService) {
		this.userService = userService;
		this.userRoleService = userRoleService;
	}

	@PostMapping
	@ApiOperation(value = "分页查询用户信息")
	public JsonResult list(@Valid @RequestBody PageVo<UserSearchVo> page, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			String errMsg = bindingResult.getFieldErrors().get(0).getDefaultMessage();
			return JsonResult.paramError(errMsg);
		}
		Page<User> users = this.userService.findByPage(page);
		return JsonResultUtil.convert(users);
	}

	@PostMapping("/add")
	@ApiOperation(value = "新增用户")
	@EBLog("新增用户{userVo.userName}")
	public JsonResult add(@Validated(AddUserVerify.class) @RequestBody UserVo userVo, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			String errMsg = bindingResult.getFieldErrors().get(0).getDefaultMessage();
			return JsonResult.paramError(errMsg);
		}
		UpdateEnum result = this.userService.save(userVo, true);
		return JsonResultUtil.convert(result);
	}

	@PostMapping("/update")
	@ApiOperation(value = "更新用户信息")
	@EBLog("更新用户{userVo.userName}信息")
	public JsonResult update(@Validated(UpdateUserVerify.class) @RequestBody UserVo userVo, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			String errMsg = bindingResult.getFieldErrors().get(0).getDefaultMessage();
			return JsonResult.paramError(errMsg);
		}
		UpdateEnum result = this.userService.update(userVo);
		return JsonResultUtil.convert(result);
	}

	@PostMapping("updatePwd")
	@ApiOperation(value = "修改密码")
	@EBLog("执行修改密码操作")
	public JsonResult updatePwd(@Valid @RequestBody PwdVo pwdVo, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			String errMsg = bindingResult.getFieldErrors().get(0).getDefaultMessage();
			return JsonResult.paramError(errMsg);
		}
		UpdateEnum result = this.userService.updatePwd(pwdVo);
		return JsonResultUtil.convert(result);
	}

	@PostMapping("/updateState")
	@ApiOperation(value = "修改用户状态")
	@EBLog("修改用户{userStateVo.userName}状态为{userStateVo.stateText}")
	public JsonResult updateState(@Valid @RequestBody UserStateVo userStateVo, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			String errMsg = bindingResult.getFieldErrors().get(0).getDefaultMessage();
			return JsonResult.paramError(errMsg);
		}
		UpdateEnum result = this.userService.updateUserState(userStateVo);
		return JsonResultUtil.convert(result);
	}

	@GetMapping("/role/{userName}")
	@ApiOperation(value = "查询用户拥有的所有角色ID")
	public JsonResult findRoles(@PathVariable("userName") String userName) {
		Set<String> roles = this.userRoleService.findByUserName(userName);
		return JsonResult.ok(roles);
	}

	@PostMapping("/role")
	@ApiOperation(value = "角色管理")
	public JsonResult relateRoles(@Valid @RequestBody UserRoleVo userRoleVo, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			String errMsg = bindingResult.getFieldErrors().get(0).getDefaultMessage();
			return JsonResult.paramError(errMsg);
		}
		UpdateEnum result = this.userRoleService.updateUserRoles(userRoleVo);
		return JsonResultUtil.convert(result);
	}
}
