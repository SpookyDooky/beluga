package com.beluga.properties;

import com.beluga.integration_test.BaseIntegrationTest;
import com.beluga.service.job.JobDefinitionService;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.TestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestPropertySource(properties = "spring.profiles.include=yaml-jobs")
class ConfiguredJobsIntegrationTest extends BaseIntegrationTest {

	private static final String JOB_NAME = "configured-job";
	
	@Autowired
	private JobDefinitionService jobDefinitionService;
	
	@TestTemplate
	void shouldCreateJobDefinitionsForConfigurationFromProperties() {
		Awaitility.await()
				.atMost(Duration.ofSeconds(10))
				.untilAsserted(() -> assertTrue(jobDefinitionService.existsByName(JOB_NAME)));
	}
}
