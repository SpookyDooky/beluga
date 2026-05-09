package com.x.scrape.persistence.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
		basePackages = "com.x.scrape"
)
public class PersistenceConfig {
}
