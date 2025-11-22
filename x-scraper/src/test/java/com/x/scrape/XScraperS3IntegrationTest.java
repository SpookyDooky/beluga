package com.x.scrape;

import com.x.scrape.integration_test.BaseS3IntegrationTest;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

import static org.mockito.Mockito.mockStatic;

public class XScraperS3IntegrationTest extends BaseS3IntegrationTest {
	
	@Test
	void shouldStart() {
		try (final MockedStatic<SpringApplication> springApplicationMockedStatic = mockStatic(SpringApplication.class)) {
			XScraper.main();
			springApplicationMockedStatic.verify(() -> SpringApplication.run(XScraper.class));
		}
	}
}
