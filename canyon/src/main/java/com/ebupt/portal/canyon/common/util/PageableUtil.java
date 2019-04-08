package com.ebupt.portal.canyon.common.util;

import com.ebupt.portal.canyon.common.vo.PageVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * 分页操作工具类
 *
 * @author chy
 * @date 2019-03-22 17:15
 */
public class PageableUtil {

	/**
	 * 根据PageVo对象获取分页信息
	 *
	 * @param page
	 *              PageVo对象
	 * @param <T>
	 *              泛型
	 * @return
	 *              pageable对象
	 */
	public static <T> Pageable getPageable(PageVo<T> page) {
		Pageable pageable;
		// 分页
		if (StringUtils.isEmpty(page.getOrder())) {
			pageable = PageRequest.of(page.getPageIdx(), page.getPageSize());
		} else {
			Sort.Direction desc = Sort.Direction.DESC;
			if (SystemConstant.SORT_ASC.equals(page.getOrderType())) {
				desc = Sort.Direction.ASC;
			}
			Sort sort = Sort.by(desc, page.getOrder());
			pageable = PageRequest.of(page.getPageIdx(), page.getPageSize(), sort);
		}
		return pageable;
	}

}
