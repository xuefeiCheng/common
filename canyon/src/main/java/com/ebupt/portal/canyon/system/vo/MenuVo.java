package com.ebupt.portal.canyon.system.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/**
 * 菜单信息
 *
 * @author chy
 * @date 2019-03-10 17:06
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MenuVo {

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String menuId;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String menuName;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String parentMenu;

}
