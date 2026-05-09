package com.beluga.properties;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class BelugaScraperPropertiesTest {
	
	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withUserConfiguration(TestConfig.class);
	
	@Test
	void shouldBeValidated() {
		contextRunner.withPropertyValues(
				"beluga.jobs[0].name=name",
				"beluga.jobs[0].storage.format=JSON",
				"beluga.jobs[0].storage.folder=/scraping/"
		).run(context -> {
			assertThat(context).hasFailed();
		});
	}
	
	@Test
	void shouldValidateInnerPersistenceProperties() {
		contextRunner.withPropertyValues(
				"beluga.persistence.type=POSTGRESQL",
				"beluga.persistence.postgresql.username=username"
		).run(context -> {
			assertThat(context).hasFailed();
		});
	}
	
	@ParameterizedTest
	@MethodSource
	void shouldBeInvalidForMisconfiguredJobProperties(final List<String> properties) {
		contextRunner.withPropertyValues(
				properties.toArray(new String[0])
		).run(context -> {
			assertThat(context).hasFailed();
		});
	}
	
	static Stream<Arguments> shouldBeInvalidForMisconfiguredJobProperties() {
		final List<String> persistenceProperties = List.of(
				"beluga.persistence.type=POSTGRESQL",
				"beluga.persistence.postgresql.username=username",
				"beluga.persistence.postgresql.password=password",
				"beluga.persistence.postgresql.url=http://localhost:1234"
		);
		
		return Stream.of(
						List.of(
								"beluga.jobs[0].name=name",
								"beluga.jobs[0].scraping.element-selector=selector",
								"beluga.jobs[0].scraping.data-points[0].selector=",
								"beluga.jobs[0].scraping.data-points[0].property-name=property-name",
								"beluga.jobs[0].storage.format=JSON",
								"beluga.jobs[0].storage.folder=folder",
								"beluga.jobs[0].storage.file=file"
						),
						List.of(
								"beluga.jobs[0].name=",
								"beluga.jobs[0].scraping.element-selector=selector",
								"beluga.jobs[0].scraping.data-points[0].selector=selector",
								"beluga.jobs[0].scraping.data-points[0].property-name=property-name",
								"beluga.jobs[0].storage.format=JSON",
								"beluga.jobs[0].storage.folder=folder",
								"beluga.jobs[0].storage.file=file"
						),
						List.of(
								"beluga.jobs[0].name=name",
								"beluga.jobs[0].scraping.element-selector=",
								"beluga.jobs[0].scraping.data-points[0].selector=selector",
								"beluga.jobs[0].scraping.data-points[0].property-name=property-name",
								"beluga.jobs[0].storage.format=JSON",
								"beluga.jobs[0].storage.folder=folder",
								"beluga.jobs[0].storage.file=file"
						),
						List.of(
								"beluga.jobs[0].name=name",
								"beluga.jobs[0].scraping.element-selector=selector",
								"beluga.jobs[0].scraping.data-points[0].selector=selector",
								"beluga.jobs[0].scraping.data-points[0].property-name=property-name",
								"beluga.jobs[0].storage.format=JSON",
								"beluga.jobs[0].storage.folder=",
								"beluga.jobs[0].storage.file=file"
						)
				).map(ArrayList::new)
				.peek(properties -> properties.addAll(persistenceProperties))
				.map(Arguments::of);
	}
	
	@EnableConfigurationProperties(BelugaScraperProperties.class)
	static class TestConfig {
	
	}
}
