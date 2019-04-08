package com.ebupt.portal.canyon.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 登录信息
 *
 * @author chy
 * @date 2019-03-10 15:27
 */
@Setter
@Getter
@ApiModel(description = "登录信息")
public class LoginVo {

	@NotBlank(message = "登录用户名不能为空")
	@ApiModelProperty(value = "登录用户名", required = true)
	private String userName;

	@NotBlank(message = "登录密码不能为空")
	@ApiModelProperty(value = "登录密码", required = true)
	private String password;

	@NotBlank(message = "验证码不能为空")
	@ApiModelProperty(value = "验证码", required = true)
	private String verifyCode;

}
