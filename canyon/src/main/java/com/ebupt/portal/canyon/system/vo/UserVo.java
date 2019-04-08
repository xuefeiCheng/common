package com.ebupt.portal.canyon.system.vo;

import com.ebupt.portal.canyon.common.annotation.CheckLength;
import com.ebupt.portal.canyon.system.verify.AddUserVerify;
import com.ebupt.portal.canyon.system.verify.UpdateUserVerify;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 更新用户信息
 *
 * @author chy
 * @date 2019-03-08 17:32
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "更新用户信息")
public class UserVo {

	@CheckLength(min = 3, max = 50, message = "用户名长度需在3~50位之间", groups = {AddUserVerify.class, UpdateUserVerify.class})
	@NotBlank(message = "用户名不能为空", groups = {AddUserVerify.class, UpdateUserVerify.class})
	@ApiModelProperty(value = "用户名")
	private String userName;

	@CheckLength(min = 6, message = "登录密码长度需大于6位", groups = {AddUserVerify.class})
	@NotBlank(message = "登录密码不能为空", groups = {AddUserVerify.class})
	@ApiModelProperty(value = "登录密码")
	private String password;

	@CheckLength(max = 50, message = "真实姓名长度需在1~50位之间", groups = {AddUserVerify.class, UpdateUserVerify.class})
	@ApiModelProperty(value = "真实姓名")
	private String realName;

	@CheckLength(max = 200, message = "所属部门长度需在1~200位之间", groups = {AddUserVerify.class, UpdateUserVerify.class})
	@ApiModelProperty(value = "所属部门")
	private String department;

	@CheckLength(max = 50, message = "联系方式长度需在1~50位之间", groups = {AddUserVerify.class, UpdateUserVerify.class})
	@ApiModelProperty(value = "联系方式")
	private String contact;

	@NotNull(message = "密码有效期不能为空", groups = {AddUserVerify.class, UpdateUserVerify.class})
	@Min(value = 0, message = "非法的密码有效期", groups = {AddUserVerify.class, UpdateUserVerify.class})
	@Max(value = 10000, message = "密码有效期过长，请输入0(永久有效)", groups = {AddUserVerify.class, UpdateUserVerify.class})
	@ApiModelProperty(value = "密码有效期")
	private Integer pwdValidity;

}
