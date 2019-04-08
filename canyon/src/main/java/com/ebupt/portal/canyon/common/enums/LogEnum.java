package com.ebupt.portal.canyon.common.enums;

/**
 * 日志类型枚举
 *
 * @author chy
 * @date 2019-03-08 15:27
 */
public enum LogEnum {
	/**
	 * 普通日志，自动添加操作用户
	 */
	LOG,

	/**
	 * 自定义日志，不添加任何信息
	 */
	CUSTOME,

	/**
	 * 无权限日志
	 */
	NO_AUTH,

	/**
	 * 未登录日志
	 */
	NO_LOGIN,

	/**
	 * 退出登录日志
	 */
	LOGOUT
}
