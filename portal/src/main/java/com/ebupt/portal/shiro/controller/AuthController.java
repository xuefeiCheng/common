package com.ebupt.portal.shiro.controller;

import com.ebupt.portal.common.Results.JSONResult;
import com.ebupt.portal.common.utils.StringUtil;
import com.ebupt.portal.common.utils.VerifyCodeUtil;
import com.ebupt.portal.log.annotations.EBLog;
import com.ebupt.portal.log.enums.LogEnum;
import com.ebupt.portal.shiro.Service.AuthService;
import com.ebupt.portal.shiro.pojo.LoginInfo;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;

@RequestMapping("/auth")
@RestController
@Slf4j
@Api(value = "/auth", description = "权限相关操作Controller")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @ApiOperation(value = "生成验证码", notes = "输出六位随机验证码有干扰图片,五分钟有效")
    @GetMapping("/code")
    public void verifyCodeImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");

        // 生成随机字符串
        String verifyCode = VerifyCodeUtil.generateVerifyCode(4);
        // 存入会话session
        HttpSession session = request.getSession(true);
        session.setAttribute("code", verifyCode.toLowerCase());
        session.setAttribute("code_time", System.currentTimeMillis());

        // 生成图片
        int width = 100, height = 30;
        OutputStream os = response.getOutputStream();
        VerifyCodeUtil.outputImage(width, height, os, verifyCode);
    }

    @ApiOperation(value = "校验验证码", notes = "校验输入验证码是否正确")
    @ApiImplicitParam(name = "code", value = "用户输入验证码", required = true)
    @GetMapping("/check/{code}")
    public JSONResult checkVerifyCode(@PathVariable("code") String code, HttpServletRequest request) {
        JSONResult result;
        // 获取正确的验证码
        HttpSession session = request.getSession(true);
        String verifyCode = (String) session.getAttribute("code");
        Long timestamp = (Long) session.getAttribute("code_time");

        if (verifyCode == null)
            result = JSONResult.ERROR("验证码已失效");
        else {
            long time_consuming = System.currentTimeMillis() - timestamp; // 耗时 ms
            if (time_consuming > 5 * 60 * 1000) {
                result = JSONResult.ERROR("验证码已过期");
                session.removeAttribute("code");
                session.removeAttribute("code_time");
            } else if (!verifyCode.equalsIgnoreCase(code)) {
                result = JSONResult.ERROR("验证码错误");
            } else {
                result = JSONResult.OK();
                session.removeAttribute("code");
                session.removeAttribute("code_time");
            }
        }

        return result;
    }

    @EBLog(value = "用户{info.username}执行登录操作", type = LogEnum.CUSTOM)
    @ApiOperation(value = "用户登录校验", notes = "判断用户输入用户名和密码是否正确")
    @PostMapping("/login")
    public JSONResult login(@RequestBody @ApiParam(required = true, value = "用户登录信息") LoginInfo info,
                            HttpServletRequest request) {
        JSONResult result;

        if (info == null) {
            result = JSONResult.PARAM_ERROR("非法参数");
        } else if (StringUtil.isEmpty(info.getUsername())) {
            result = JSONResult.PARAM_ERROR("参数[username]不能为空");
        } else if (StringUtil.isEmpty(info.getPassword())) {
            result = JSONResult.PARAM_ERROR("参数[password]不能为空");
        } else {
            result = this.authService.checkLogin(info.getUsername(), info.getPassword(), request);
        }

        return result;
    }

    @EBLog(type = LogEnum.LOGOUT)
    @ApiOperation(value = "退出登录", notes = "用户退出登录操作")
    @GetMapping("/logout")
    public JSONResult logout() {
        Subject subject = SecurityUtils.getSubject(); // 获取用户主体
        if (subject != null) // 已登录则注销登录
            subject.logout();

        return JSONResult.OK();
    }

    @EBLog(type = LogEnum.NO_AUTH)
    @GetMapping("/noauth")
    public JSONResult noauth() {
        return JSONResult.NO_AUTH();
    }

    @EBLog(type = LogEnum.NO_LOGIN)
    @GetMapping("/nologin")
    public JSONResult nologin() {
        return JSONResult.NO_LOGIN();
    }
}
