package com.ebupt.portal.canyon.system.vo;

import com.ebupt.portal.canyon.common.annotation.CheckLength;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * 新增角色信息
 *
 * @author chy
 * @date 2019-03-22 17:50
 */
@Setter
@Getter
@ApiModel("角色新增信息")
public class RoleVo {

	@NotBlank(message = "角色ID不能为空")
	@CheckLength(min = 3, max = 50, message = "角色ID长度需在1~50位之间")
	@ApiModelProperty(value = "角色ID", required = true)
	private String roleId;

	@NotBlank(message = "角色名称不能为空")
	@CheckLength(min = 3, max = 50, message = "角色名称长度需在1~50位之间")
	@ApiModelProperty(value = "角色名称", required = true)
	private String roleName;
}
