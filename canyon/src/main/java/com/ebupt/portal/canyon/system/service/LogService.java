package com.ebupt.portal.canyon.system.service;

import com.ebupt.portal.canyon.common.enums.UpdateEnum;
import com.ebupt.portal.canyon.common.vo.PageVo;
import com.ebupt.portal.canyon.system.entity.Log;
import com.ebupt.portal.canyon.system.vo.LogSearchVo;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

/**
 * 日志处理业务层接口
 *
 * @author chy
 * @date 2019-03-08 16:30
 */
public interface LogService {

	/**
	 * 保存日志记录
	 *
	 * @param log
	 *              日志记录
	 */
	void save(Log log);

	/**
	 * 分页查询日志记录
	 *
	 * @param pageVo
	 *                  分页查询条件
	 * @return
	 *                  日志集合
	 */
	Page<Log> findByPage(PageVo<LogSearchVo> pageVo);

	/**
	 * 删除指定时间之前的日志记录
	 *
	 * @param daysAgo
	 *                  时长
	 * @return
	 *                  操作结果
	 */
	@Transactional(rollbackFor = Exception.class)
	UpdateEnum deleteByTime(int daysAgo);
}
