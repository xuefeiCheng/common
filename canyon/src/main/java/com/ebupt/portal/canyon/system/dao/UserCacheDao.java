package com.ebupt.portal.canyon.system.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

/**
 * 用户相关缓存
 *
 * @author chy
 * @date 2019-03-10 16:21
 */
@Slf4j
@CacheConfig(cacheNames = "auth_times")
@Repository("userCacheDao")
public class UserCacheDao {

	@Cacheable(key = "#userName")
	public int getLoginErrorTimes(String userName, int loginErrorTimes) {
		if (log.isDebugEnabled()) {
			log.debug("查询并缓存用户{}登录失败次数，已失败{}次", userName, loginErrorTimes);
		}
		return loginErrorTimes;
	}

	@CachePut(key = "#userName")
	public int updateLoginErrorTimes(String userName, int loginErrorTimes) {
		if (log.isDebugEnabled()) {
			log.debug("缓存用户{}登录失败次数，已失败{}次", userName, loginErrorTimes);
		}
		return loginErrorTimes;
	}

	@CacheEvict(key = "#userName")
	public void deleteLoginErrorTimes(String userName) {}

}
