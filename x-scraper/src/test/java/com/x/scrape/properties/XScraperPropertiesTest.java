package com.x.scrape.properties;

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

class XScraperPropertiesTest {
	
	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withUserConfiguration(TestConfig.class);
	
	@Test
	void shouldBeValidated() {
		contextRunner.withPropertyValues(
				"x-scraper.jobs[0].name=name",
				"x-scraper.jobs[0].storage.format=JSON",
				"x-scraper.jobs[0].storage.folder=/scraping/"
		).run(context -> {
			assertThat(context).hasFailed();
		});
	}
	
	@Test
	void shouldValidateInnerPersistenceProperties() {
		contextRunner.withPropertyValues(
				"x-scraper.persistence.type=POSTGRESQL",
				"x-scraper.persistence.postgresql.username=username"
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
				"x-scraper.persistence.type=POSTGRESQL",
				"x-scraper.persistence.postgresql.username=username",
				"x-scraper.persistence.postgresql.password=password",
				"x-scraper.persistence.postgresql.url=http://localhost:1234"
		);
		
		return Stream.of(
						List.of(
								"x-scraper.jobs[0].name=name",
								"x-scraper.jobs[0].scraping.element-selector=selector",
								"x-scraper.jobs[0].scraping.data-points[0].selector=",
								"x-scraper.jobs[0].scraping.data-points[0].property-name=property-name",
								"x-scraper.jobs[0].storage.format=JSON",
								"x-scraper.jobs[0].storage.folder=folder",
								"x-scraper.jobs[0].storage.file=file"
						),
						List.of(
								"x-scraper.jobs[0].name=",
								"x-scraper.jobs[0].scraping.element-selector=selector",
								"x-scraper.jobs[0].scraping.data-points[0].selector=selector",
								"x-scraper.jobs[0].scraping.data-points[0].property-name=property-name",
								"x-scraper.jobs[0].storage.format=JSON",
								"x-scraper.jobs[0].storage.folder=folder",
								"x-scraper.jobs[0].storage.file=file"
						),
						List.of(
								"x-scraper.jobs[0].name=name",
								"x-scraper.jobs[0].scraping.element-selector=",
								"x-scraper.jobs[0].scraping.data-points[0].selector=selector",
								"x-scraper.jobs[0].scraping.data-points[0].property-name=property-name",
								"x-scraper.jobs[0].storage.format=JSON",
								"x-scraper.jobs[0].storage.folder=folder",
								"x-scraper.jobs[0].storage.file=file"
						),
						List.of(
								"x-scraper.jobs[0].name=name",
								"x-scraper.jobs[0].scraping.element-selector=selector",
								"x-scraper.jobs[0].scraping.data-points[0].selector=selector",
								"x-scraper.jobs[0].scraping.data-points[0].property-name=property-name",
								"x-scraper.jobs[0].storage.format=JSON",
								"x-scraper.jobs[0].storage.folder=",
								"x-scraper.jobs[0].storage.file=file"
						)
				).map(ArrayList::new)
				.peek(properties -> properties.addAll(persistenceProperties))
				.map(Arguments::of);
	}
	
	@EnableConfigurationProperties(XScraperProperties.class)
	static class TestConfig {
	
	}
}
