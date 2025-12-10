package com.x.scrape.api.execution.controller;

import com.x.scrape.api.execution.service.ExecutionApiService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ExecutionControllerTest {
	
	@Mock
	private ExecutionApiService executionApiService;
	
	@InjectMocks
	private ExecutionController executionController;
	
	@Test
	void shouldStart() {
		final Long jobId = 123L;
		
		final ResponseEntity<Void> response = executionController.start(jobId);
		
		assertEquals(204, response.getStatusCode().value());
		verify(executionApiService).start(jobId);
	}
	
	@Test
	void shouldStop() {
		final Long jobId = 123L;
		
		final ResponseEntity<Void> response = executionController.stop(jobId);
		
		assertEquals(204, response.getStatusCode().value());
		verify(executionApiService).stop(jobId);
	}
	
	@Test
	void shouldPause() {
		final Long jobId = 123L;
		
		final ResponseEntity<Void> response = executionController.pause(jobId);
		
		assertEquals(204, response.getStatusCode().value());
		verify(executionApiService).pause(jobId);
	}
	
	@Test
	void shouldResume() {
		final Long jobId = 123L;
		
		final ResponseEntity<Void> response = executionController.resume(jobId);
		
		assertEquals(204, response.getStatusCode().value());
		verify(executionApiService).resume(jobId);
	}
}