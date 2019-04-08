package com.ebupt.portal.canyon.system.entity;

import com.ebupt.portal.canyon.common.util.TimeUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * 角色权限实体类
 *
 * @author chy
 * @date 2019-03-08 14:52
 */
@Entity
@Table(name = "eb_role_menu")
@Setter
@Getter
@NoArgsConstructor
public class RoleMenu {

	@Id
	@JsonIgnore
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * 角色ID
	 */
	@Column(length = 50, nullable = false)
	private String roleId;

	/**
	 * 菜单ID
	 */
	@Column(length = 50, nullable = false)
	private String menuId;

	/**
	 * 创建时间
	 */
	@Column(length = 14, nullable = false)
	private String createTime;

	public RoleMenu(String roleId, String menuId) {
		this.roleId = roleId;
		this.menuId = menuId;
		this.createTime = TimeUtil.getCurrentTime14();
	}
}
