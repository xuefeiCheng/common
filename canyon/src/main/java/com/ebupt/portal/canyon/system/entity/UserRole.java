package com.ebupt.portal.canyon.system.entity;

import com.ebupt.portal.canyon.common.util.TimeUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * 用户角色信息实体类
 *
 * @author chy
 * @date 2019-03-08 14:45
 */
@Entity
@Table(name = "eb_user_role")
@Setter
@Getter
@NoArgsConstructor
public class UserRole {

	@Id
	@JsonIgnore
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * 登录用户名
	 */
	@Column(length = 50, nullable = false)
	private String userName;

	/**
	 * 角色ID
	 */
	@Column(length = 50, nullable = false)
	private String roleId;

	/**
	 * 创建时间
	 */
	@Column(length = 14, nullable = false)
	private String createTime;

	public UserRole(String userName, String roleId) {
		this.userName = userName;
		this.roleId = roleId;
		this.createTime = TimeUtil.getCurrentTime14();
	}
}
