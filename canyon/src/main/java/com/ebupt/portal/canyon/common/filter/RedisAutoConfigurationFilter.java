package com.ebupt.portal.canyon.common.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import javax.validation.constraints.NotNull;

/**
 * redis自动配置类排除过滤器
 *
 * @author chy
 * @date 2019-03-07 16:23
 */
@Slf4j
public class RedisAutoConfigurationFilter implements TypeFilter {

	private static final String CACHE_TYPE = System.getProperty("CACHE_TYPE");
	private static final String CACHE_REDIS = "redis";
	private static final String REDIS_CONFIG = ".RedisAutoConfiguration";


	@Override
	public boolean match(@NotNull MetadataReader metadataReader,
	                     @NotNull MetadataReaderFactory metadataReaderFactory) {
		String className = metadataReader.getClassMetadata().getClassName();
		if (!CACHE_REDIS.equals(CACHE_TYPE)
				&& metadataReader.getAnnotationMetadata().hasAnnotation(Configuration.class.getName())) {
			if (log.isDebugEnabled()) {
				log.debug("scan configuration: {}", className);
			}
			if (className.endsWith(REDIS_CONFIG)) {
				log.info("使用ehcache缓存，关闭redis自动配置");
				return true;
			}
		} else {
			if (log.isDebugEnabled()) {
				log.debug("scan bean: {}", className);
			}
		}
		return false;
	}
}
