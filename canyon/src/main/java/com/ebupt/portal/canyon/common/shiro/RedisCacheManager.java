package com.ebupt.portal.canyon.common.shiro;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * redis CacheManger
 *
 * @author chy
 * @date 2019-03-14 18:14
 */
@Component
public class RedisCacheManager implements CacheManager {

	private final RedisTemplate<String, Object> redisTemplate;

	@Lazy
	@Autowired
	public RedisCacheManager(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	public Cache getCache(String s) throws CacheException {
		return new RedisCache<>(redisTemplate, s);
	}
}
