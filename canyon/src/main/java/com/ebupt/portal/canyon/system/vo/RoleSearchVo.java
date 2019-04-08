package com.ebupt.portal.canyon.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 角色查询信息
 *
 * @author chy
 * @date 2019-03-22 17:01
 */
@Setter
@Getter
@ApiModel("角色查询信息")
public class RoleSearchVo {

	@ApiModelProperty("角色名称")
	private String roleName;

	@ApiModelProperty("开始时间")
	private String startTime;

	@ApiModelProperty("结束时间")
	private String endTime;

}
