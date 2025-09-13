package com.x.scrape.storage;

import com.x.scrape.model.job.Job;
import com.x.scrape.model.task.event.TaskCompletedEvent;
import com.x.scrape.scraping.job.JobRegistry;
import com.x.scrape.storage.io.FileWritingService;
import com.x.scrape.storage.json.JsonService;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StorageServiceTest {
	
	@Mock
	private JsonService jsonService;
	@Mock
	private FileWritingService fileWritingService;
	@Mock
	private JobRegistry jobRegistry;
	
	@InjectMocks
	private StorageService storageService;
	
	@Test
	void shouldSaveResultOnTaskCompletedEvent() {
		final TaskCompletedEvent taskCompletedEvent = Instancio.create(TaskCompletedEvent.class);
		final Job job = Instancio.create(Job.class);
		when(jobRegistry.get(taskCompletedEvent.getJobId())).thenReturn(job);
		
		final String jsonContent = "json";
		when(jsonService.toJson(taskCompletedEvent.getResult())).thenReturn(jsonContent);
		
		storageService.onTaskCompleted(taskCompletedEvent);
		
		verify(fileWritingService).write(
				job.getStorageConfiguration().getFolder(),
				taskCompletedEvent.getTaskId() + ".json",
				jsonContent
		);
	}
}