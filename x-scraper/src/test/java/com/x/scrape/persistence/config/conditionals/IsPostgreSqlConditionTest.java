package com.x.scrape.persistence.config.conditionals;

import com.x.scrape.properties.persistence.PersistenceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.EnumSet;
import java.util.stream.Stream;

import static com.x.scrape.properties.persistence.PersistenceType.POSTGRESQL;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
	
	@ParameterizedTest
	@MethodSource
	void shouldNotMatch(final PersistenceType persistenceType) {
		when(environment.getProperty("x-scraper.persistence.type")).thenReturn(persistenceType.name());
		assertFalse(condition.matches(context, metadata));
	}
	
	static Stream<Arguments> shouldNotMatch() {
		return EnumSet.complementOf(EnumSet.of(POSTGRESQL))
				.stream()
				.map(Arguments::of);
	}
}