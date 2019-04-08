package com.ebupt.portal.canyon;

import com.ebupt.portal.canyon.common.filter.RedisAutoConfigurationFilter;
import com.ebupt.portal.canyon.common.listener.ApplicationStartedEventListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * spring boot配置、启动类
 *
 * @author chy
 * @date 2019-02-27 16:02
 */
@EnableCaching
@EnableSwagger2
@EnableScheduling
@ComponentScan(basePackages = {"com.ebupt.portal.canyon", "org.springframework.boot.autoconfigure.data.redis"},
		excludeFilters = {
			@ComponentScan.Filter(type = FilterType.CUSTOM, classes = {RedisAutoConfigurationFilter.class})
})
@SpringBootApplication
public class CanyonApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(CanyonApplication.class);
		app.addListeners(new ApplicationStartedEventListener());
		app.run(args);
	}

}
