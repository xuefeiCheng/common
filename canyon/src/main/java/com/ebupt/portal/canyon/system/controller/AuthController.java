package com.ebupt.portal.canyon.system.controller;

import com.ebupt.portal.canyon.common.annotation.EBLog;
import com.ebupt.portal.canyon.common.dto.JsonResult;
import com.ebupt.portal.canyon.common.enums.LogEnum;
import com.ebupt.portal.canyon.system.service.UserService;
import com.ebupt.portal.canyon.system.util.VerifyCodeUtil;
import com.ebupt.portal.canyon.system.vo.LoginVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 用户权限管理控制层
 *
 * @author chy
 * @date 2019-03-10 13:25
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@Api(description = "用户权限管理")
@ApiResponses({@ApiResponse(code = 200, message = "请求成功", response = JsonResult.class)})
public class AuthController {

	private final UserService userService;

	@Autowired
	public AuthController(UserService userService) {
		this.userService = userService;
	}

	@ApiOperation(value = "生成验证码", notes = "随机生成验证码")
	@GetMapping("/code")
	public void verifyCodeImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 设置页面不缓存验证码
		// 应用于http 1.0和1.1
		response.setHeader("Pragma", "no-cache");
		// 只能应用于http 1.1
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");

		// 生成随机字符串
		String verifyCode = VerifyCodeUtil.generateVerifyCode(4);
		// 存入session中
		HttpSession session = request.getSession();
		session.setAttribute("code", verifyCode.toLowerCase());
		session.setAttribute("code_time", System.currentTimeMillis());

		// 生成图片
		int width = 100, height = 30;
		OutputStream os = response.getOutputStream();
		VerifyCodeUtil.outputImage(width, height, os, verifyCode);
	}

	@PostMapping("/login")
	@EBLog(value = "用户{loginVo.userName}尝试登录本系统", type = LogEnum.CUSTOME)
	@ApiOperation(value = "用户登录", notes = "用户登录")
	public JsonResult login(HttpServletRequest request, @Valid @RequestBody LoginVo loginVo,
	                        BindingResult bindingResult) {
		// 参数校验
		if (bindingResult.hasErrors()) {
			String errorMsg = bindingResult.getFieldErrors().get(0).getDefaultMessage();
			return JsonResult.paramError(errorMsg);
		}
		return this.userService.login(request, loginVo);
	}

	@GetMapping("/logout")
	@EBLog(type = LogEnum.LOGOUT)
	@ApiOperation(value = "退出登录", notes = "退出登录")
	public JsonResult logout() {
		Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated()) {
			subject.logout();
			return JsonResult.ok();
		}
		return JsonResult.error("请先登录");
	}

	@GetMapping("/noAuth")
	@EBLog(type = LogEnum.NO_AUTH)
	public JsonResult noAuth() {
		return JsonResult.error(401, "无访问权限");
	}

	@GetMapping("/noLogin")
	@EBLog(type = LogEnum.NO_LOGIN)
	public JsonResult noLogin() {
		return JsonResult.error(402, "请登录后访问");
	}

}
