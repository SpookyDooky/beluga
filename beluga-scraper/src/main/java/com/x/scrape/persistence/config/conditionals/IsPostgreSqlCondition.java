package com.x.scrape.persistence.config.conditionals;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

import static com.x.scrape.properties.persistence.PersistenceType.POSTGRESQL;

public class IsPostgreSqlCondition implements Condition {
	
	@Override
	public boolean matches(final ConditionContext context,
	                       final AnnotatedTypeMetadata metadata) {
		final Environment environment = context.getEnvironment();
		final String persistenceType = environment.getProperty("x-scraper.persistence.type");
		
		return POSTGRESQL.name().equals(persistenceType);
	}
}
