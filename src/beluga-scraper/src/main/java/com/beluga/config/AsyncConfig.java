package com.beluga.config;

import com.beluga.async.CustomAsyncTaskExecutor;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * This config makes sure that there is a custom async thread pool task executor that is used for {@link Async}
 * annotated methods. The custom implementation of {@link CustomAsyncTaskExecutor} makes sure the {@link MDC} is clear
 * for each new task.
 */
@Configuration
@EnableAsync
public class AsyncConfig {
	
	@Bean
	public Executor taskExecutor() {
		final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

		executor.setCorePoolSize(256);
		executor.setMaxPoolSize(1024);
		executor.setQueueCapacity(0);
		executor.setThreadNamePrefix("async-executor-");
		executor.initialize();
		
		return new CustomAsyncTaskExecutor(executor);
	}
}
