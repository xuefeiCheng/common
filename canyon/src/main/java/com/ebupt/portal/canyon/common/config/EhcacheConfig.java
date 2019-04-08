package com.ebupt.portal.canyon.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * ehcache配置类
 *
 * @author chy
 * @date 2019-03-15 10:22
 */
@Configuration
public class EhcacheConfig {

	@Value("${spring.cache.ehcache.config:config/ehcache.xml}")
	private String config;

	private static final String SPLIT = ":";

	@Bean
	public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
		if (config.indexOf(SPLIT) >= 0) {
			config = config.split(":")[1];
		}

		EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();
		ehCacheManagerFactoryBean.setConfigLocation(new ClassPathResource(config));
		// 将EhCacheManager由单实例改成多实例
		ehCacheManagerFactoryBean.setShared(true);
		return ehCacheManagerFactoryBean;
	}

}
