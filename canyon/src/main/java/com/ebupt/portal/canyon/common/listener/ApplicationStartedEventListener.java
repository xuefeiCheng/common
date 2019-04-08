package com.ebupt.portal.canyon.common.listener;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import javax.validation.constraints.NotNull;

/**
 * 系统启动监听器，进行配置初始化
 *
 * @author chy
 * @date 2019-03-01 11:24
 */
public class ApplicationStartedEventListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent>, Ordered {

	private static final String LOG_PATH = "logging.log-path";
	private static final String LOG_LEVEL = "logging.log-level";
	private static final String SAVE_DURATION = "logging.save-duration";
	private static final String CACHE_TYPE = "spring.cache.type";

	@Override
	public void onApplicationEvent(@NotNull ApplicationEnvironmentPreparedEvent event) {
		ConfigurableEnvironment configurableEnvironment = event.getEnvironment();

		MutablePropertySources mutablePropertySources = configurableEnvironment.getPropertySources();

		PropertySource<?> propertySource = mutablePropertySources.get("configurationProperties");

		if (propertySource != null && propertySource.containsProperty(LOG_PATH)) {
			String logPath = null;
			if (propertySource.containsProperty(LOG_PATH)) {
				logPath = (String) propertySource.getProperty(LOG_PATH);
			}
			if (StringUtils.isEmpty(logPath)) {
				logPath = "log";
			}
			System.setProperty("LOG_PATH", logPath);

			String saveDuration = null;
			if (propertySource.containsProperty(SAVE_DURATION)) {
				saveDuration = (String) propertySource.getProperty(SAVE_DURATION);
			}
			if (StringUtils.isEmpty(saveDuration)) {
				saveDuration = "30d";
			}
			System.setProperty("SAVE_DURATION", saveDuration);

			String logLevel = null;
			if (propertySource.containsProperty(LOG_LEVEL)) {
				logLevel = (String) propertySource.getProperty(LOG_LEVEL);
			}
			if (StringUtils.isEmpty(logLevel)) {
				logLevel = "debug";
			}
			System.setProperty("LOG_LEVEL", logLevel);

			String cacheType = null;
			if (propertySource.containsProperty(CACHE_TYPE)) {
				cacheType = (String) propertySource.getProperty(CACHE_TYPE);
			}
			if (StringUtils.isEmpty(cacheType)) {
				cacheType = "ehcache";
			}
			System.setProperty("CACHE_TYPE", cacheType);
		}
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE + 10;
	}

}
