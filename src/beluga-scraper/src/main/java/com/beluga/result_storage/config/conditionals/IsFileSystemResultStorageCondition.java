package com.beluga.result_storage.config.conditionals;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

import static com.beluga.properties.datastore.DataStoreType.FILE_SYSTEM;

public class IsFileSystemResultStorageCondition implements Condition {
	
	@Override
	public boolean matches(final ConditionContext context,
	                       final AnnotatedTypeMetadata metadata) {
		final Environment environment = context.getEnvironment();
		final String resultStorageType = environment.getProperty("beluga.result-storage.type");
		
		return FILE_SYSTEM.name().equalsIgnoreCase(resultStorageType);
	}
}
