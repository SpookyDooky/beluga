package com.beluga;

import com.beluga.integration_test.BaseIntegrationTest;
import org.junit.jupiter.api.TestTemplate;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

import static org.mockito.Mockito.mockStatic;

public class BelugaScraperIntegrationTest extends BaseIntegrationTest {

	@TestTemplate
	void shouldStart() {
		try (final MockedStatic<SpringApplication> springApplicationMockedStatic = mockStatic(SpringApplication.class)) {
			BelugaScraper.main();
			springApplicationMockedStatic.verify(() -> SpringApplication.run(BelugaScraper.class));
		}
	}
}
