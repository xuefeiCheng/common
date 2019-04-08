package com.ebupt.portal.canyon.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 用户状态信息
 *
 * @author chy
 * @date 2019-03-22 10:57
 */
@Setter
@Getter
@ApiModel("用户状态信息")
public class UserStateVo {

	@NotBlank(message = "用户名不能为空")
	@ApiModelProperty(value = "用户名", required = true)
	private String userName;

	@NotBlank(message = "状态不能为空")
	@Pattern(regexp = "^([1|4])$", message = "非法的状态码")
	@ApiModelProperty(value = "状态", required = true, allowableValues = "1, 4")
	private String state;

	@NotBlank(message = "状态说明不能为空")
	@ApiModelProperty(value = "状态描述", required = true)
	private String stateText;

}
