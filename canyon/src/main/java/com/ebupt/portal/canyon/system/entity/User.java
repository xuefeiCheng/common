package com.ebupt.portal.canyon.system.entity;

import com.ebupt.portal.canyon.common.util.EncryptUtil;
import com.ebupt.portal.canyon.common.util.TimeUtil;
import com.ebupt.portal.canyon.system.vo.UserVo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * 用户信息实体类
 *
 * @author chy
 * @date 2019-03-07 20:30
 */
@Entity
@Table(name = "eb_user")
@Setter
@Getter
@NoArgsConstructor
public class User {

	@Id
	@JsonIgnore
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * 登录用户名
	 */
	@Column(length = 50, nullable = false, unique = true)
	private String userName;

	/**
	 * 真实姓名
	 */
	@Column(length = 50)
	private String realName;

	/**
	 * 登录密码
	 */
	@JsonIgnore
	@Column(length = 32, nullable = false)
	private String password;

	/**
	 * MD5加密盐值
	 */
	@JsonIgnore
	@Column(length = 6, nullable = false)
	private String salt;

	/**
	 * 所属部门
	 */
	@Column(length = 200)
	private String department;

	/**
	 * 联系方式
	 */
	@Column(length = 50)
	private String contact;

	/**
	 * 创建时间
	 */
	@Column(length = 14, nullable = false)
	private String createTime;

	/**
	 * 最后一次登录的时间
	 */
	@Column(length = 14)
	private String lastLoginTime;

	/**
	 * 最后一次修改密码时间
	 */
	@Column(length = 14)
	private String lastPwdTime;

	/**
	 * 密码有效期 0-永久有效 >1 有效天数
	 */
	@Column(nullable = false)
	private Integer pwdValidity;

	/**
	 * 用户状态 1-正常 2-密码已过期 3-多次登录失败锁定 4-注销
	 */
	@Column(length = 1, nullable = false)
	private String state;

	public User(UserVo user) {
		String now = TimeUtil.getCurrentTime14();
		String salt = EncryptUtil.randomSalt(6);

		this.userName = user.getUserName();
		this.realName = user.getRealName();
		this.password = EncryptUtil.md5WithSpecialSalt(user.getPassword(), salt);
		this.salt = salt;
		this.department = user.getDepartment();
		this.contact = user.getContact();
		this.createTime = now;
		this.pwdValidity = user.getPwdValidity();
		this.state = "1";
	}
}
