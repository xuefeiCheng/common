package com.ebupt.portal.canyon.system.vo;

import com.ebupt.portal.canyon.common.annotation.CheckLength;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.Set;

/**
 * 用户角色关联信息
 *
 * @author chy
 * @date 2019-03-22 15:54
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("用户角色关联信息")
public class UserRoleVo {

	@NotBlank(message = "用户名不能为空")
	@CheckLength(min = 3, max = 50, message = "用户名长度需在3~50位之间")
	@ApiModelProperty(value = "用户名", required = true)
	private String userName;

	@NotBlank(message = "操作类型不能为空")
	@Pattern(regexp = "^(add|delete)$", message = "非法的操作类型")
	@ApiModelProperty(value = "操作类型", required = true, allowableValues = "add,delete")
	private String op;

	@NotEmpty(message = "角色ID不能为空")
	@ApiModelProperty(value = "角色ID集合", required = true)
	private Set<String> roleIds;

}
