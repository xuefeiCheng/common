package com.ebupt.portal.canyon.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 菜单权限信息实体类
 *
 * @author chy
 * @date 2019-03-08 14:37
 */
@Entity
@Table(name = "eb_menu")
@Setter
@Getter
@EqualsAndHashCode
public class Menu {

	@Id
	@JsonIgnore
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * 菜单ID
	 */
	@Column(length = 50, nullable = false, unique = true)
	private String menuId;

	/**
	 * 菜单名称
	 */
	@Column(length = 50, nullable = false)
	private String menuName;

	/**
	 * 菜单类型 1-菜单 2-按钮
	 */
	@Column(length = 1, nullable = false)
	private String menuType;

	/**
	 * 上级菜单ID
	 */
	@Column(length = 50)
	private String parentMenu;

	/**
	 * 创建时间
	 */
	@Column(length = 14, nullable = false)
	private String createTime;

	/**
	 * url
	 */
	@Column(length = 100)
	private String url;
}
