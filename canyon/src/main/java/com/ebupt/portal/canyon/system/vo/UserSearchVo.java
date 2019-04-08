package com.ebupt.portal.canyon.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;

/**
 * 用户查询信息
 *
 * @author chy
 * @date 2019-03-20 14:31
 */
@Setter
@Getter
@ApiModel("用户查询信息")
public class UserSearchVo {

	@ApiModelProperty("用户名")
	private String userName;

	@ApiModelProperty("开始时间")
	private String startTime;

	@ApiModelProperty("结束时间")
	private String endTime;

	@Pattern(regexp = "^([1234])$", message = "非法的用户状态")
	@ApiModelProperty(value = "用户状态", allowableValues = "1, 2, 3, 4")
	private String state;

}
