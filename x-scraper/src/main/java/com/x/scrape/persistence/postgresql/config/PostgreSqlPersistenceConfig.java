package com.x.scrape.persistence.postgresql.config;

import com.x.scrape.persistence.config.conditionals.annotation.IsPostgreSql;
import com.x.scrape.properties.persistence.PostgreSqlPersistenceProperties;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

@IsPostgreSql
@Configuration
public class PostgreSqlPersistenceConfig {
	
	@Bean
	public PlatformTransactionManager transactionManager(final EntityManagerFactory emf) {
		return new JpaTransactionManager(emf);
	}
	
	@Bean
	public DataSource dataSource(final PostgreSqlPersistenceProperties properties) {
		final HikariDataSource dataSource = new HikariDataSource();
		
		dataSource.setJdbcUrl(properties.getUrl());
		dataSource.setUsername(properties.getUsername());
		dataSource.setPassword(properties.getPassword());
		dataSource.setDriverClassName(properties.getDriverClassName());
		
		return dataSource;
	}
	
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(final DataSource dataSource,
	                                                                   final JpaVendorAdapter jpaVendorAdapter) {
		final LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactory.setDataSource(dataSource);
		entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter);
		
		final Properties jpaProperties = new Properties();
		jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
		entityManagerFactory.setJpaProperties(jpaProperties);
		
		return entityManagerFactory;
	}
}
