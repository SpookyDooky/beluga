package com.x.scrape.scraping.job;

import com.x.scrape.http.HttpService;
import com.x.scrape.model.job.Job;
import com.x.scrape.model.task.Task;
import com.x.scrape.scraping.DocumentScrapingService;
import com.x.scrape.scraping.task.JobTaskQueue;
import com.x.scrape.scraping.task.TaskFactory;
import com.x.scrape.scraping.worker.Worker;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobExecutionService {
	
	private final Logger logger;
	private final HttpService httpService;
	private final DocumentScrapingService documentScrapingService;
	private final TaskFactory taskFactory;
	private final JobTaskQueue jobTaskQueue;
	private final ApplicationEventPublisher applicationEventPublisher;
	
	public JobExecutionService(final Logger logger,
	                           final HttpService httpService,
	                           final DocumentScrapingService documentScrapingService,
	                           final TaskFactory taskFactory,
	                           final JobTaskQueue jobTaskQueue,
	                           final ApplicationEventPublisher applicationEventPublisher) {
		this.logger = logger;
		this.httpService = httpService;
		this.documentScrapingService = documentScrapingService;
		this.taskFactory = taskFactory;
		this.jobTaskQueue = jobTaskQueue;
		this.applicationEventPublisher = applicationEventPublisher;
	}
	
	public void executeJob(final Job job) {
		logger.info("Executing job");
		
		final List<Task> tasks = job.getUrlConfiguration()
				.getUrls()
				.stream()
				.map(url -> taskFactory.create(url, job))
				.toList();
		
		tasks.forEach(jobTaskQueue::offerTask);
		
		for (int i = 0; i < 4; i++) {
			final Worker worker = new Worker(
					job.getId(),
					jobTaskQueue,
					documentScrapingService,
					httpService,
					applicationEventPublisher
			);
			
			new Thread(worker::start).start();;
		}
	}
}
