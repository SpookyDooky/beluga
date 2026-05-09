package com.beluga.persistence.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
		basePackages = "com.beluga.scrape"
)
public class PersistenceConfig {
}
