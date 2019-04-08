package com.ebupt.portal.canyon.system.entity;

import com.ebupt.portal.canyon.common.util.TimeUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * 角色信息实体类
 *
 * @author chy
 * @date 2019-03-08 14:23
 */
@Entity
@Table(name = "eb_role")
@Setter
@Getter
@NoArgsConstructor
public class Role {

	@Id
	@JsonIgnore
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * 角色ID
	 */
	@Column(length = 50, nullable = false, unique = true)
	private String roleId;

	/**
	 * 角色名称
	 */
	@Column(length = 50, nullable = false)
	private String roleName;

	/**
	 * 创建时间
	 */
	@Column(length = 14, nullable = false)
	private String createTime;

	public Role(String roleId, String roleName) {
		this.roleId = roleId;
		this.roleName = roleName;
		this.createTime = TimeUtil.getCurrentTime14();
	}
}
