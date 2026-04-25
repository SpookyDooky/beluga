package com.x.scrape.integration_test;

import com.x.scrape.integration_test.properties.TestContainerProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;

@MultiStoreTest
@SpringBootTest
@Transactional
@Rollback
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BaseIntegrationTest {
	
	@DynamicPropertySource
	static void dynamicProperties(final DynamicPropertyRegistry registry) {
		TestContainerProperties.overrideProperties(registry);
	}
}
