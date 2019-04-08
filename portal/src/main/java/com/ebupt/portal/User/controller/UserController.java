package com.ebupt.portal.User.controller;

import com.ebupt.portal.User.entity.User;
import com.ebupt.portal.User.service.UserService;
import com.ebupt.portal.common.Results.JSONResult;
import com.ebupt.portal.log.annotations.EBLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/user")
@Api(value = "/user", description = "用户管理Controller")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @ApiOperation(value = "用户列表", notes = "查询所有用户信息")
    @EBLog("查询所有用户")
    public JSONResult getAllUser() {
        return this.userService.findAll();
    }

    @GetMapping("/{name}")
    @ApiOperation(value = "用户查询", notes = "根据用户名查询用户信息")
    @EBLog("查询用户{name}")
    public JSONResult getUserByName(@PathVariable("name") String name) {
        return this.userService.findByName(name);
    }

    @PostMapping("/edit")
    @ApiOperation(value = "修改用户信息", notes = "根据用户编号修改用户信息")
    @EBLog("修改用户{user.name}信息")
    public JSONResult updateUser(@RequestBody User user) {
        return this.userService.update(user);
    }

    @GetMapping("/delete/{id}")
    @ApiOperation(value = "删除用户信息", notes = "根据用户ID删除用户信息")
    @EBLog("删除用户{id}")
    public JSONResult deleteById(@PathVariable("id") int id) {
        return this.userService.deleteById(id);
    }

}
