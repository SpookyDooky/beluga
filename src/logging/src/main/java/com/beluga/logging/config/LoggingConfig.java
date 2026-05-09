package com.beluga.logging.config;

import com.beluga.logging.ContextLogger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Configuration
@ComponentScan
public class LoggingConfig {
	
	@Bean
	@Scope(SCOPE_PROTOTYPE)
	public ContextLogger logger(final InjectionPoint injectionPoint) {
		final Class<?> clazz = injectionPoint.getMember().getDeclaringClass();
		return new ContextLogger(LogManager.getLogger(clazz));
	}
}
