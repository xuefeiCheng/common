package com.ebupt.portal.canyon.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 分页查询条件
 *
 * @author chy
 * @date 2019-03-20 14:19
 */
@Setter
@Getter
@ApiModel("分页查询条件")
public class PageVo<T> {

	@NotNull(message = "页码不能为空")
	@Min(value = 0, message = "非法的页码")
	@ApiModelProperty(value = "页码，0表示第一页", required = true)
	private Integer pageIdx = 0;

	@NotNull(message = "页大小不能为空")
	@Min(value = 10, message = "非法的页大小")
	@ApiModelProperty(value = "页大小", required = true)
	private Integer pageSize;

	@ApiModelProperty(value = "排序字段")
	private String order;

	@Pattern(regexp = "^(asc|desc)$", message = "非法的排序方式")
	@ApiModelProperty(value = "排序方式", allowableValues = "asc, desc")
	private String orderType = "desc";

	@Valid
	@ApiModelProperty(value = "查询条件")
	private T search;

}
