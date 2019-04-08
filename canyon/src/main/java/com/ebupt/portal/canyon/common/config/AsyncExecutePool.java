package com.ebupt.portal.canyon.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 自定义异步线程池
 *
 * @author chy
 * @date 2019-03-25 20:22
 */
@EnableAsync
@Configuration
public class AsyncExecutePool {

	@Bean("taskExecute")
	public ThreadPoolTaskExecutor taskExecute() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		// 线程池核心线程数
		executor.setCorePoolSize(10);
		// 线程池维护的线程的最大数量
		executor.setMaxPoolSize(20);
		// 缓存队列长度
		executor.setQueueCapacity(10);
		// 非核心线程的允许的空闲时长
		executor.setKeepAliveSeconds(60);
		// 被拒绝任务的处理机制：CallerRunsPolicy-直接在execute方法的调用线程中运行被拒绝的任务
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		// 线程前缀
		executor.setThreadNamePrefix("canyon-async-executor-");
		executor.initialize();
		return executor;
	}

}
