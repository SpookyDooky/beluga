package com.x.scrape;

import com.x.scrape.integration_test.BaseIntegrationTest;
import org.junit.jupiter.api.TestTemplate;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

import static org.mockito.Mockito.mockStatic;

public class XScraperIntegrationTest extends BaseIntegrationTest {
	// TODO - Add a full integration test for the entire system that actually scrapes data from a mock web server
	@TestTemplate
	void shouldStart() {
		try (final MockedStatic<SpringApplication> springApplicationMockedStatic = mockStatic(SpringApplication.class)) {
			XScraper.main();
			springApplicationMockedStatic.verify(() -> SpringApplication.run(XScraper.class));
		}
	}
}
