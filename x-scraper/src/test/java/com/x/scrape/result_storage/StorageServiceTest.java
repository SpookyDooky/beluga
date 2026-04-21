package com.x.scrape.result_storage;

import com.x.scrape.logging.ContextLogger;
import com.x.scrape.model.event.storable.payload.ImagePayload;
import com.x.scrape.model.event.storable.payload.JsonPayload;
import com.x.scrape.model.event.storable.payload.Payload;
import com.x.scrape.execution.event.task.task_result.TaskResultEvent;
import com.x.scrape.result_storage.json.JsonService;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.nio.file.Path;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StorageServiceTest {
	
	@Mock
	private ContextLogger logger;
	@Mock
	private JsonService jsonService;
	@Mock
	private ResultDataStoreProvider resultDataStoreProvider;
	
	@InjectMocks
	private StorageService storageService;
	
	@Test
	void shouldSaveJsonResult() {
		final TaskResultEvent jsonTaskResultEvent = createTaskResultEvent(JsonPayload.class);
		
		final String json = "json";
		final JsonPayload mapPayload = (JsonPayload) jsonTaskResultEvent.getPayload();
		when(jsonService.toJson(mapPayload.getData())).thenReturn(json);
		
		storageService.onTaskResultEvent(jsonTaskResultEvent);
		
		verify(resultDataStoreProvider).save(
				jsonTaskResultEvent.getStorageHint().getPath(),
				json.getBytes()
		);
	}
	
	TaskResultEvent createTaskResultEvent(final Class<? extends Payload> payloadClass) {
		return Instancio.of(TaskResultEvent.class)
				.set(field(TaskResultEvent::getPayload), Instancio.create(payloadClass))
				.create();
	}
	
	@Test
	void shouldSaveInputStreamResult() {
		final TaskResultEvent imageTaskResultEvent = createTaskResultEvent(ImagePayload.class);
		final ImagePayload imagePayload = (ImagePayload) imageTaskResultEvent.getPayload();
		
		storageService.onTaskResultEvent(imageTaskResultEvent);
		
		verify(resultDataStoreProvider).save(
				eq(imageTaskResultEvent.getStorageHint().getPath()),
				any(ByteArrayInputStream.class)
		);
	}
	
	@Test
	void shouldRetrieve() {
		final Path pathToRetrieve = Path.of("");
		final byte[] expected = new byte[0];
		when(resultDataStoreProvider.retrieve(pathToRetrieve)).thenReturn(expected);
		
		final byte[] result = storageService.retrieve(pathToRetrieve);
		
		assertSame(expected, result);
	}
}