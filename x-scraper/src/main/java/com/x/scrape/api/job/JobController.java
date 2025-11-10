package com.x.scrape.api.job;

import com.x.scrape.logging.ContextLogger;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jobs")
public class JobController {
	
	private ContextLogger logger;
	
	@PostMapping
	@Transactional
	public void createJob() {
	
	}
}
