package com.ebupt.portal.canyon.common.util;

import com.ebupt.portal.canyon.common.dto.JsonResult;
import com.ebupt.portal.canyon.common.enums.UpdateEnum;
import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JsonResult工具类，转换数据结构为JsonResult类型
 *
 * @author chy
 * @date 2019-03-19 21:16
 */
public class JsonResultUtil {

	/**
	 * 将枚举UpdateEnum类型转成JsonResult类型
	 *
	 * @param result
	 *                  UpdateEnum枚举
	 * @return
	 *                  JsonResult对象
	 */
	public static JsonResult convert(UpdateEnum result) {
		if (result.getCode() == SystemConstant.UPDATE_ENUM_SUCCESS) {
			return JsonResult.ok();
		} else {
			return JsonResult.error(result.getMsg());
		}
	}

	/**
	 * 将Page对象转成JsonResult类型
	 *
	 * @param page
	 *              Page对象
	 * @param <T>
	 *              泛型
	 * @return
	 *              JsonResult对象
	 */
	public static <T> JsonResult convert(Page<T> page) {
		Map<String, Object> map = new HashMap<>(5);
		long count = page.getTotalElements();
		List<T> list = page.getContent();

		map.put("count", count);
		map.put("list", list);
		return JsonResult.ok(map);
	}

}
