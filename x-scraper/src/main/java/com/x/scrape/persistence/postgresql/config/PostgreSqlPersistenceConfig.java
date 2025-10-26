package com.x.scrape.persistence.postgresql.config;

import com.x.scrape.persistence.config.conditionals.annotation.IsPostgreSql;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@IsPostgreSql
@Configuration
public class PostgreSqlPersistenceConfig {
	
	@Bean
	public PlatformTransactionManager transactionManager(final EntityManagerFactory emf) {
		return new JpaTransactionManager(emf);
	}
}
