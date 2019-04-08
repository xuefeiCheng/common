package com.ebupt.portal.canyon.system.vo;

import com.ebupt.portal.canyon.common.annotation.CheckLength;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * 修改密码信息
 *
 * @author chy
 * @date 2019-03-19 17:47
 */
@Setter
@Getter
@ApiModel("修改密码信息")
public class PwdVo {

	@NotBlank(message = "用户名不能为空")
	@ApiModelProperty(value = "用户名", required = true)
	private String userName;

	@NotBlank(message = "旧密码不能为空")
	@ApiModelProperty(value = "旧密码", required = true)
	private String oldPwd;

	@NotBlank(message = "新密码不能为空")
	@CheckLength(min = 32, max = 32, message = "新密码需为32位字符")
	@ApiModelProperty(value = "新密码", required = true)
	private String newPwd;
}
