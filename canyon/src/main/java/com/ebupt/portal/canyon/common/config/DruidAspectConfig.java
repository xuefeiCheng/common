package com.ebupt.portal.canyon.common.config;

import com.alibaba.druid.support.spring.stat.DruidStatInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.JdkRegexpMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Scope;

/**
 * Druid spring监控配置
 *
 * @author chy
 * @date 2019-03-01 15:28
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class DruidAspectConfig {

	@Bean
	public DruidStatInterceptor druidStatInterceptor() {
		return new DruidStatInterceptor();
	}

	@Bean
	@Scope("prototype")
	public JdkRegexpMethodPointcut druidStatPointcut() {
		JdkRegexpMethodPointcut pointcut = new JdkRegexpMethodPointcut();
		pointcut.setPatterns("com.ebupt.portal.canyon.*.service.*");
		return pointcut;
	}

	@Bean
	public DefaultPointcutAdvisor druidStatAdvisor(DruidStatInterceptor druidStatInterceptor,
	                                               JdkRegexpMethodPointcut pointcut) {
		DefaultPointcutAdvisor defaultPointcutAdvisor = new DefaultPointcutAdvisor();
		defaultPointcutAdvisor.setPointcut(pointcut);
		defaultPointcutAdvisor.setAdvice(druidStatInterceptor);
		return defaultPointcutAdvisor;
	}

}
