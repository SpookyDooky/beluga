package com.x.scrape.api.job.controller;

import com.x.scrape.integration_test.BaseIntegrationTest;
import org.junit.jupiter.api.TestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@AutoConfigureMockMvc
public class JobControllerIntegrationTest extends BaseIntegrationTest {
	
	@Autowired
	private MockMvc mvc;
	
	@TestTemplate
	void shouldReturn404() throws Exception {
		final Long jobId = 123L;
		
		mvc.perform(MockMvcRequestBuilders.get("/jobs/" + jobId))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}
}
