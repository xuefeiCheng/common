package com.ebupt.portal.canyon.common.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * redis配置类
 *
 * @author chy
 * @date 2019-03-06 14:37
 */
@Configuration
@ConditionalOnProperty(prefix = "spring.cache", name = "type", havingValue = "redis")
public class RedisConfig {

	/**
	 * 设置默认的缓存管理器
	 *
	 * @param redisConnectionFactory
	 *                                  redis连接工厂对象
	 * @return
	 *                                  cacheManager对象
	 */
	@Bean
	public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
		// 设置缓存时间为1小时
		RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration
				.defaultCacheConfig().entryTtl(Duration.ofHours(1))
				.disableCachingNullValues().computePrefixWith(cacheName -> cacheName + ":")
				.serializeKeysWith(RedisSerializationContext.SerializationPair
						.fromSerializer(new StringRedisSerializer()));
		return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory)
				.cacheDefaults(cacheConfiguration).build();

	}

	/**
	 * 配置RedisTemplate序列化方式，不配置则只能使用StringRedisTemplate
	 *
	 * @param redisConnectionFactory
	 *                                  redis连接工厂对象
	 * @return
	 *                                  RedisTemplate对象
	 */
	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		// 配置RedisTemplate
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		// 配置连接工厂
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		RedisSerializer stringSerializer = new StringRedisSerializer();
		redisTemplate.setKeySerializer(stringSerializer);
		redisTemplate.setHashKeySerializer(stringSerializer);
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}
}
