package com.beluga.persistence.config.conditionals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

import static com.beluga.properties.persistence.PersistenceType.POSTGRESQL;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IsPostgreSqlConditionTest {
	
	@Mock
	private ConditionContext context;
	@Mock
	private AnnotatedTypeMetadata metadata;
	@Mock
	private Environment environment;
	
	private final IsPostgreSqlCondition condition = new IsPostgreSqlCondition();
	
	@BeforeEach
	void setup() {
		when(context.getEnvironment()).thenReturn(environment);
	}
	
	@Test
	void shouldMatch() {
		when(environment.getProperty("x-scraper.persistence.type")).thenReturn(POSTGRESQL.name());
		assertTrue(condition.matches(context, metadata));
	}
	
}