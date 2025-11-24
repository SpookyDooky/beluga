package com.x.scrape;

import com.x.scrape.integration_test.BaseIntegrationTest;
import org.junit.jupiter.api.TestTemplate;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

import static org.mockito.Mockito.mockStatic;

public class XScraperIntegrationTest extends BaseIntegrationTest {
	
	@TestTemplate
	void shouldStart() {
		try (final MockedStatic<SpringApplication> springApplicationMockedStatic = mockStatic(SpringApplication.class)) {
			XScraper.main();
			springApplicationMockedStatic.verify(() -> SpringApplication.run(XScraper.class));
		}
	}
}
