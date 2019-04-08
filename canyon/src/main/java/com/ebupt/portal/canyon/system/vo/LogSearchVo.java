package com.ebupt.portal.canyon.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;

/**
 * 日志查询信息
 *
 * @author chy
 * @date 2019-03-24 17:41
 */
@Setter
@Getter
@ApiModel("日志查询信息")
public class LogSearchVo {

	@ApiModelProperty(value = "操作用户")
	private String userName;

	@ApiModelProperty(value = "查询开始时间")
	private String startTime;

	@ApiModelProperty(value = "查询结束时间")
	private String endTime;

	@Pattern(regexp = "^([123])$", message = "非法的操作结果")
	@ApiModelProperty(value = "操作结果", allowableValues = "[1, 2, 3]")
	private String type;

}
