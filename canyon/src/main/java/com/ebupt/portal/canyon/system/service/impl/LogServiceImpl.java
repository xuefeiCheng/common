package com.ebupt.portal.canyon.system.service.impl;

import com.ebupt.portal.canyon.common.enums.UpdateEnum;
import com.ebupt.portal.canyon.common.util.PageableUtil;
import com.ebupt.portal.canyon.common.util.TimeUtil;
import com.ebupt.portal.canyon.common.vo.PageVo;
import com.ebupt.portal.canyon.system.dao.LogDao;
import com.ebupt.portal.canyon.system.entity.Log;
import com.ebupt.portal.canyon.system.service.LogService;
import com.ebupt.portal.canyon.system.vo.LogSearchVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * 日志处理业务层实现
 *
 * @author chy
 * @date 2019-03-08 16:31
 */
@Service("logService")
public class LogServiceImpl implements LogService {

	private final LogDao logDao;

	@Autowired
	public LogServiceImpl(LogDao logDao) {
		this.logDao = logDao;
	}

	@Override
	public void save(Log log) {
		this.logDao.save(log);
	}

	@Override
	public Page<Log> findByPage(PageVo<LogSearchVo> pageVo) {
		LogSearchVo logSearch = pageVo.getSearch();
		Specification<Log> specification = ((root, query, criteriaBuilder) -> {
			List<Predicate> list = new ArrayList<>();

			if (logSearch != null) {
				if (StringUtils.isNotEmpty(logSearch.getUserName())) {
					list.add(criteriaBuilder.like(root.get("operator"), logSearch.getUserName() + "%"));
				}
				if (StringUtils.isNotEmpty(logSearch.getStartTime())) {
					list.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createTime"), logSearch.getStartTime()));
				}
				if (StringUtils.isNotEmpty(logSearch.getEndTime())) {
					list.add(criteriaBuilder.lessThanOrEqualTo(root.get("createTime"), logSearch.getEndTime()));
				}
				if (StringUtils.isNotEmpty(logSearch.getType())) {
					list.add(criteriaBuilder.equal(root.get("result"), logSearch.getType()));
				}
			}

			Predicate[] predicates = new Predicate[list.size()];
			return query.where(list.toArray(predicates)).getRestriction();
		});

		return this.logDao.findAll(specification, PageableUtil.getPageable(pageVo));
	}

	@Override
	public UpdateEnum deleteByTime(int daysAgo) {
		String time = TimeUtil.getBeforeDays(daysAgo) + "000000";
		this.logDao.deleteByTime(time);
		return UpdateEnum.SUCCESS;
	}
}
