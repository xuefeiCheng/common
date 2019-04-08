package com.ebupt.portal.canyon.common.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 自定义redis cache
 *
 * @author chy
 * @date 2019-03-14 18:08
 */
@Slf4j
public class RedisCache<K, V> implements Cache<K, V> {

	private RedisTemplate<K, V> redisTemplate;
	private String cacheKey;

	RedisCache(RedisTemplate<K, V> redisTemplate, String cacheKey) {
		this.redisTemplate = redisTemplate;
		this.cacheKey = cacheKey;
	}

	@Override
	public V get(K k) throws CacheException {
		redisTemplate.boundValueOps(getCacheKey(k)).expire(60, TimeUnit.SECONDS);
		return redisTemplate.boundValueOps(getCacheKey(k)).get();
	}

	@Override
	public V put(K k, V v) throws CacheException {
		V old = get(k);
		redisTemplate.boundValueOps(getCacheKey(k)).set(v);
		return old;
	}

	@Override
	public V remove(K k) throws CacheException {
		V old = get(k);
		redisTemplate.delete(getCacheKey(k));
		return old;
	}

	@Override
	public void clear() throws CacheException {
		redisTemplate.delete(keys());
	}

	@Override
	public int size() {
		return keys().size();
	}

	@Override
	public Set<K> keys() {
		return redisTemplate.keys(getCacheKey("*"));
	}

	@Override
	public Collection<V> values() {
		Set<K> set = keys();
		List<V> list = new ArrayList<>();
		for (K s: set) {
			list.add(get(s));
		}
		return list;
	}

	private K getCacheKey(Object k) {
		return (K) (this.cacheKey + ":" + k);
	}
}
