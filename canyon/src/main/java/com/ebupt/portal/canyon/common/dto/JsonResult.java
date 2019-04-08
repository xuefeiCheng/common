package com.ebupt.portal.canyon.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

/**
 * 对返回的JSON数据进行封装
 *
 * @author chy
 * @date 2019-02-27 17:14
 */
@Setter
@Getter
public class JsonResult {

	private int code;
	private String msg;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Object data;

	private JsonResult(int code, String msg, Object data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	/**
	 * 请求成功，无返回数据
	 *
	 * @return
	 *              JsonResult对象
	 */
	public static JsonResult ok() {
		return JsonResult.ok(null);
	}

	/**
	 * 请求成功，存在返回数据
	 *
	 * @param data
	 *                  请求数据
	 * @return
	 *                  JsonResult对象
	 */
	public static JsonResult ok(Object data) {
		return new JsonResult(200, "OK", data);
	}

	/**
	 * 请求失败，返回失败原因
	 *
	 * @param msg
	 *                  失败原因
	 * @return
	 *                  JsonResult对象
	 */
	public static JsonResult error(String msg) {
		return error(500, msg);
	}

	/**
	 * 请求失败，自定义返回码
	 *
	 * @param code
	 *              失败返回码
	 * @param msg
	 *              失败原因
	 * @return
	 *              JsonResult对象
	 */
	public static JsonResult error(int code, String msg) {
		return new JsonResult(code, msg, null);
	}

	/**
	 * 参数校验失败
	 *
	 * @param msg
	 *              失败原因
	 * @return
	 *              JsonResult对象
	 */
	public static JsonResult paramError(String msg) {
		return new JsonResult(400, msg, null);
	}

}
