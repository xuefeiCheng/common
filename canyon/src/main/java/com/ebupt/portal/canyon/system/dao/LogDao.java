package com.ebupt.portal.canyon.system.dao;

import com.ebupt.portal.canyon.system.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 日志处理持久层
 *
 * @author chy
 * @date 2019-03-08 16:32
 */
public interface LogDao extends JpaRepository<Log, Long>, JpaSpecificationExecutor<Log> {

	/**
	 * 删除指定时间之前的数据
	 *
	 * @param time
	 *              时间
	 */
	@Modifying
	@Query("delete from Log l where l.createTime < :time")
	void deleteByTime(@Param("time") String time);
}
