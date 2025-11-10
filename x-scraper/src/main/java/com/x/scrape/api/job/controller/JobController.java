package com.x.scrape.api.job.controller;

import com.x.scrape.api.job.dto.write.WriteJobDto;
import com.x.scrape.logging.ContextLogger;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jobs")
public class JobController {
	
	private final ContextLogger logger;
	
	public JobController(final ContextLogger logger) {
		this.logger = logger;
	}
	
	@PostMapping
	@Transactional
	public void createJob(@RequestBody final WriteJobDto job) {
	
	}
}
