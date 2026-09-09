package com.beluga.persistence.sqlite.config;

import com.beluga.persistence.config.conditionals.annotation.IsSqlite;
import com.beluga.persistence.sqlite.converter.InstantConverter;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.boot.flyway.autoconfigure.FlywayConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@IsSqlite
public class SqlitePersistenceConfig {
	
	private static final String DATABASE_LOCATION = "/app/db/sqlite.db";
	
	@Bean
	public PlatformTransactionManager transactionManager(final EntityManagerFactory emf) {
		return new JpaTransactionManager(emf);
	}
	
	@Bean
	public DataSource dataSource() {
		final HikariDataSource dataSource = new HikariDataSource();

		dataSource.setJdbcUrl("jdbc:sqlite:" + DATABASE_LOCATION + "?foreign_keys=on");
		dataSource.setDriverClassName("org.sqlite.JDBC");
		dataSource.setMaximumPoolSize(1);
		
		return dataSource;
	}
	
	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		final HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		
		adapter.setShowSql(false);
		adapter.setGenerateDdl(false);
		adapter.setDatabasePlatform("org.hibernate.community.dialect.SQLiteDialect");
		
		return adapter;
	}
	
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(final DataSource dataSource,
	                                                                   final JpaVendorAdapter jpaVendorAdapter) {
		final LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactory.setDataSource(dataSource);
		entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter);
		entityManagerFactory.setPackagesToScan("com.beluga");
		
		final Properties jpaProperties = new Properties();
		jpaProperties.put("hibernate.physical_naming_strategy", "org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy");
		jpaProperties.put("hibernate.dialect", "org.hibernate.community.dialect.SQLiteDialect");
		entityManagerFactory.setJpaProperties(jpaProperties);
		
		return entityManagerFactory;
	}
	
	@Bean
	public FlywayConfigurationCustomizer flywayCustomizer() {
		return configuration -> {
			configuration.createSchemas(false);
			configuration.schemas();
			configuration.defaultSchema(null);
			configuration.locations("classpath:db/sqlite");
		};
	}
	
	@Bean
	public InstantConverter instantConverter() {
		return new InstantConverter();
	}
}
