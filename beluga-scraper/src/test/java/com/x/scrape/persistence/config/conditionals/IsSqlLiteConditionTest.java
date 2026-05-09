package com.x.scrape.persistence.config.conditionals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

import static com.x.scrape.properties.persistence.PersistenceType.SQL_LITE;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IsSqlLiteConditionTest {
	
	@Mock
	private ConditionContext context;
	@Mock
	private AnnotatedTypeMetadata metadata;
	@Mock
	private Environment environment;
	
	private final IsSqlLiteCondition condition = new IsSqlLiteCondition();
	
	@BeforeEach
	void setup() {
		when(context.getEnvironment()).thenReturn(environment);
	}
	
	@Test
	void shouldMatch() {
		when(environment.getProperty("x-scraper.persistence.type")).thenReturn(SQL_LITE.name());
		assertTrue(condition.matches(context, metadata));
	}
	
}