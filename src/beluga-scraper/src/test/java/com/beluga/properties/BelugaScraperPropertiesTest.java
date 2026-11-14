package com.beluga.properties;

import com.beluga.properties.scraping.extraction_configuration.AttributeExtractionConfigurationProperties;
import com.beluga.properties.scraping.extraction_configuration.DescriptionListExtractionConfigurationProperties;
import com.beluga.properties.scraping.extraction_configuration.ExtractionConfigurationProperties;
import com.beluga.properties.scraping.extraction_configuration.HtmlExtractionConfigurationProperties;
import com.beluga.properties.scraping.extraction_configuration.ImageExtractionConfigurationProperties;
import com.beluga.properties.scraping.extraction_configuration.TextExtractionConfigurationProperties;
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
	
	private static final List<String> POSTGRESQL_PROPERTIES = List.of(
			"beluga.persistence.type=POSTGRESQL",
			"beluga.persistence.postgresql.username=username",
			"beluga.persistence.postgresql.password=password",
			"beluga.persistence.postgresql.url=http://localhost:1234"
	);
	
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
	
	@Test
	void shouldBindExtractionConfigurations() {
		final List<String> properties = withPersistence(List.of(
				"beluga.jobs[0].name=name",
				"beluga.jobs[0].scraping.item-selector=item",
				"beluga.jobs[0].scraping.data-points[0].text.selector=p.price",
				"beluga.jobs[0].scraping.data-points[0].text.field=price",
				"beluga.jobs[0].scraping.data-points[1].attribute.selector=a",
				"beluga.jobs[0].scraping.data-points[1].attribute.field=link",
				"beluga.jobs[0].scraping.data-points[1].attribute.attribute=href",
				"beluga.jobs[0].scraping.data-points[2].html.selector=div",
				"beluga.jobs[0].scraping.data-points[2].html.field=body",
				"beluga.jobs[0].scraping.data-points[3].image=",
				"beluga.jobs[0].scraping.data-points[4].description-list.selector=dl",
				"beluga.jobs[0].scraping.data-points[4].description-list.data-points[0].dt-value=Color",
				"beluga.jobs[0].scraping.data-points[4].description-list.data-points[0].field=color"
		));
		
		contextRunner.withPropertyValues(properties.toArray(new String[0])).run(context -> {
			assertThat(context).hasNotFailed();
			final List<ExtractionConfigurationProperties> dataPoints = context.getBean(BelugaScraperProperties.class)
					.getJobs().getFirst().getScraping().getDataPoints();
			
			assertThat(dataPoints).hasSize(5);
			assertThat(dataPoints.get(0).configuredExtractionType()).isInstanceOfSatisfying(TextExtractionConfigurationProperties.class, text -> {
				assertThat(text.getSelector()).isEqualTo("p.price");
				assertThat(text.getField()).isEqualTo("price");
			});
			assertThat(dataPoints.get(1).configuredExtractionType()).isInstanceOfSatisfying(AttributeExtractionConfigurationProperties.class, attribute -> {
				assertThat(attribute.getSelector()).isEqualTo("a");
				assertThat(attribute.getField()).isEqualTo("link");
				assertThat(attribute.getAttribute()).isEqualTo("href");
			});
			assertThat(dataPoints.get(2).configuredExtractionType()).isInstanceOfSatisfying(HtmlExtractionConfigurationProperties.class, html -> {
				assertThat(html.getSelector()).isEqualTo("div");
				assertThat(html.getField()).isEqualTo("body");
			});
			assertThat(dataPoints.get(3).configuredExtractionType()).isInstanceOf(ImageExtractionConfigurationProperties.class);
			assertThat(dataPoints.get(4).configuredExtractionType()).isInstanceOfSatisfying(DescriptionListExtractionConfigurationProperties.class, descriptionList -> {
				assertThat(descriptionList.getSelector()).isEqualTo("dl");
				assertThat(descriptionList.getDataPoints()).singleElement().satisfies(dataPoint -> {
					assertThat(dataPoint.getDtValue()).isEqualTo("Color");
					assertThat(dataPoint.getField()).isEqualTo("color");
				});
			});
		});
	}
	
	@ParameterizedTest
	@MethodSource
	void shouldBeInvalidForMisconfiguredJobProperties(final List<String> properties, final String expectedInvalidField) {
		contextRunner.withPropertyValues(
				properties.toArray(new String[0])
		).run(context -> {
			assertThat(context).hasFailed();
			assertThat(context.getStartupFailure()).rootCause().hasMessageContaining(expectedInvalidField);
		});
	}
	
	static Stream<Arguments> shouldBeInvalidForMisconfiguredJobProperties() {
		return Stream.of(
						Arguments.of(withPersistence(List.of(
								"beluga.jobs[0].name=name",
								"beluga.jobs[0].scraping.item-selector=selector",
								"beluga.jobs[0].scraping.data-points[0].text.selector=",
								"beluga.jobs[0].scraping.data-points[0].text.field=field"
						)), "jobs[0].scraping.dataPoints[0].text.selector"),
						Arguments.of(withPersistence(List.of(
								"beluga.jobs[0].name=",
								"beluga.jobs[0].scraping.item-selector=selector",
								"beluga.jobs[0].scraping.data-points[0].text.selector=selector",
								"beluga.jobs[0].scraping.data-points[0].text.field=field"
						)), "jobs[0].name"),
						Arguments.of(withPersistence(List.of(
								"beluga.jobs[0].name=name",
								"beluga.jobs[0].scraping.item-selector=",
								"beluga.jobs[0].scraping.data-points[0].text.selector=selector",
								"beluga.jobs[0].scraping.data-points[0].text.field=field"
						)), "jobs[0].scraping.itemSelector"),
						Arguments.of(withPersistence(List.of(
								"beluga.jobs[0].name=name",
								"beluga.jobs[0].scraping.item-selector=selector",
								"beluga.jobs[0].scraping.data-points[0].text.selector=selector",
								"beluga.jobs[0].scraping.data-points[0].text.field="
						)), "jobs[0].scraping.dataPoints[0].text.field"),
						Arguments.of(withPersistence(List.of(
								"beluga.jobs[0].name=name",
								"beluga.jobs[0].scraping.item-selector=selector",
								"beluga.jobs[0].scraping.data-points[0].text.selector=selector",
								"beluga.jobs[0].scraping.data-points[0].text.field=field",
								"beluga.jobs[0].scraping.data-points[0].image="
						)), "but found: [image, text]")
		);
	}
	
	private static List<String> withPersistence(final List<String> properties) {
		final List<String> result = new ArrayList<>(properties);
		result.addAll(POSTGRESQL_PROPERTIES);
		return result;
	}
	
	@EnableConfigurationProperties(BelugaScraperProperties.class)
	static class TestConfig {
	
	}
}
