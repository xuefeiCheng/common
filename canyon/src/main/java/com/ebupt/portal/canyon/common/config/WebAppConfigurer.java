package com.ebupt.portal.canyon.common.config;

import com.ebupt.portal.canyon.common.util.SystemConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * 静态资源映射配置
 *
 * @author chy
 * @date 2019-02-27 20:19
 */
@Configuration
public class WebAppConfigurer extends WebMvcConfigurationSupport {

	@Value("${spring.profiles.active}")
	private String env;

	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		if (SystemConstant.DEV.equals(env)) {
			registry.addResourceHandler("/swagger-ui.html")
					.addResourceLocations("classpath:/META-INF/resources/");
			registry.addResourceHandler("/webjars/**")
					.addResourceLocations("classpath:/META-INF/resources/webjars/");
		}
		registry.addResourceHandler("/favicon.ico")
				.addResourceLocations("classpath:/static/");
		super.addResourceHandlers(registry);
	}

}
