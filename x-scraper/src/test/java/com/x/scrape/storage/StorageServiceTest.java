package com.x.scrape.storage;

import com.x.scrape.logging.ContextLogger;
import com.x.scrape.model.event.storable.payload.ImagePayload;
import com.x.scrape.model.event.storable.payload.JsonPayload;
import com.x.scrape.model.event.storable.payload.Payload;
import com.x.scrape.model.task.event.task_result.StorageType;
import com.x.scrape.model.task.event.task_result.TaskResultEvent;
import com.x.scrape.storage.io.FileWritingService;
import com.x.scrape.storage.json.JsonService;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.x.scrape.model.task.event.task_result.StorageType.IMAGE;
import static com.x.scrape.model.task.event.task_result.StorageType.JSON;
import static org.instancio.Select.field;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StorageServiceTest {
	
	@Mock
	private ContextLogger logger;
	@Mock
	private JsonService jsonService;
	@Mock
	private FileWritingService fileWritingService;
	
	@InjectMocks
	private StorageService storageService;
	
	@Test
	void shouldSaveJsonResult() {
		final TaskResultEvent jsonTaskResultEvent = createTaskResultEvent(JSON, JsonPayload.class);
		
		final String json = "json";
		final JsonPayload mapPayload = (JsonPayload) jsonTaskResultEvent.getPayload();
		when(jsonService.toJson(mapPayload.getData())).thenReturn(json);
		
		storageService.onTaskResultEvent(jsonTaskResultEvent);
		
		verify(fileWritingService).write(
				jsonTaskResultEvent.getStorageHint().getFolder(),
				jsonTaskResultEvent.getStorageHint().getFileName(),
				json
		);
	}
	
	TaskResultEvent createTaskResultEvent(final StorageType storageType,
	                                      final Class<? extends Payload> payloadClass) {
		return Instancio.of(TaskResultEvent.class)
				.set(field(TaskResultEvent::getPayload), Instancio.create(payloadClass))
				.create();
	}
	
	@Test
	void shouldSaveInputStreamResult() {
		final TaskResultEvent imageTaskResultEvent = createTaskResultEvent(IMAGE, ImagePayload.class);
		final ImagePayload imagePayload = (ImagePayload) imageTaskResultEvent.getPayload();
		
		storageService.onTaskResultEvent(imageTaskResultEvent);
		
		verify(fileWritingService).write(
				imageTaskResultEvent.getStorageHint().getFolder(),
				imageTaskResultEvent.getStorageHint().getFileName(),
				imagePayload.getData()
		);
	}
}