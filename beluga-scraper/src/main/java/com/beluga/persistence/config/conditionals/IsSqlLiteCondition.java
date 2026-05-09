package com.beluga.persistence.config.conditionals;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

import static com.beluga.properties.persistence.PersistenceType.SQL_LITE;

public class IsSqlLiteCondition implements Condition {
	
	@Override
	public boolean matches(final ConditionContext context,
	                       final AnnotatedTypeMetadata metadata) {
		final Environment environment = context.getEnvironment();
		final String persistenceType = environment.getProperty("beluga.persistence.type");
		
		return SQL_LITE.name().equals(persistenceType);
	}
}
