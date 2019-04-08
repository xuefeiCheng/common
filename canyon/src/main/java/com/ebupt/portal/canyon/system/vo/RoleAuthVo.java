package com.ebupt.portal.canyon.system.vo;

import com.ebupt.portal.canyon.common.annotation.CheckLength;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 角色权限变更信息
 *
 * @author chy
 * @date 2019-03-24 16:42
 */
@Setter
@Getter
@ApiModel("角色权限变更信息")
public class RoleAuthVo {

	@NotBlank(message = "角色ID不能为空")
	@CheckLength(min = 3, max = 50, message = "角色ID长度需在1~50位之间")
	@ApiModelProperty(value = "角色ID")
	private String roleId;

	@ApiModelProperty(value = "新增的权限ID集合")
	private List<String> addNodes;

	@ApiModelProperty(value = "删除的权限ID集合")
	private List<String> removeNodes;
}
