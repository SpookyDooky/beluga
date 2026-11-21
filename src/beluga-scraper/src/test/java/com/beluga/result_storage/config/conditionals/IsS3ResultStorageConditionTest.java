package com.beluga.result_storage.config.conditionals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

import static com.beluga.properties.datastore.DataStoreType.FILE_SYSTEM;
import static com.beluga.properties.datastore.DataStoreType.S3;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IsS3ResultStorageConditionTest {
	
	@Mock
	private ConditionContext context;
	@Mock
	private AnnotatedTypeMetadata metadata;
	@Mock
	private Environment environment;
	
	private final IsS3ResultStorageCondition condition = new IsS3ResultStorageCondition();
	
	@BeforeEach
	void setup() {
		when(context.getEnvironment()).thenReturn(environment);
	}
	
	@Test
	void shouldMatch() {
		when(environment.getProperty("beluga.result-storage.type")).thenReturn(S3.name());
		assertTrue(condition.matches(context, metadata));
	}
	
	@Test
	void shouldMatchLowercaseType() {
		when(environment.getProperty("beluga.result-storage.type")).thenReturn("s3");
		assertTrue(condition.matches(context, metadata));
	}
}