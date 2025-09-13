package com.x.scrape.storage;

import com.x.scrape.model.job.Job;
import com.x.scrape.model.task.event.TaskCompletedEvent;
import com.x.scrape.scraping.job.JobRegistry;
import com.x.scrape.storage.io.FileWritingService;
import com.x.scrape.storage.json.JsonService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StorageService {
	
	private final Logger logger = LogManager.getLogger();
	
	private final JsonService jsonService;
	private final FileWritingService fileWritingService;
	private final JobRegistry jobRegistry;
	
	public StorageService(final JsonService jsonService,
	                      final FileWritingService fileWritingService,
	                      final JobRegistry jobRegistry) {
		this.jsonService = jsonService;
		this.fileWritingService = fileWritingService;
		this.jobRegistry = jobRegistry;
	}
	
	@EventListener
	@Async
	public void onTaskCompleted(final TaskCompletedEvent taskCompletedEvent) {
		logger.info("Saving results for task " + taskCompletedEvent.getTaskId() + ".");
		final Job job = jobRegistry.get(taskCompletedEvent.getJobId());
		
		final String fileName = taskCompletedEvent.getTaskId().toString() + ".json";
		saveContent(
				job.getStorageConfiguration().getFolder(),
				fileName,
				taskCompletedEvent.getResult()
		);
	}
	
	private void saveContent(final String folderName,
	                         final String fileName,
	                         final List<Map<String, Object>> content) {
		final String jsonContent = jsonService.toJson(content);
		
		fileWritingService.write(
				folderName,
				fileName,
				jsonContent
		);
	}
}
