package com.ebupt.portal.canyon.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 更新/新增操作结果
 *
 * @author chy
 * @date 2019-03-19 20:46
 */
@Getter
@AllArgsConstructor
public enum UpdateEnum {
	/**
	 * 更新/新增操作的各种结果
	 */
	SUCCESS(0, "成功"),
	UNKNOWN_ERROR(10, "未知的错误"),
	PASSWORD_ERROR(11, "原始密码错误"),
	NULL_ERROR(12, "存在空值"),
	REPEAT_NAME_ERROR(20, "该名称已存在"),
	REPEAT_ID_ERROR(21, "该ID已存在");

	private int code;
	private String msg;

}
