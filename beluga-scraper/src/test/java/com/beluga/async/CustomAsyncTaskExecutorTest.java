package com.beluga.async;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;
import org.springframework.core.task.AsyncTaskExecutor;

import java.util.concurrent.Callable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomAsyncTaskExecutorTest {
	
	@Mock
	private AsyncTaskExecutor delegate;
	
	@InjectMocks
	private CustomAsyncTaskExecutor customAsyncTaskExecutor;
	
	@Captor
	private ArgumentCaptor<Runnable> runnableArgumentCaptor;
	@Captor
	private ArgumentCaptor<Callable<String>> callableArgumentCaptor;
	
	@Mock
	private Runnable runnable;
	@Mock
	private Callable<String> callable;
	
	private MockedStatic<MDC> mdc;
	
	@BeforeEach
	void setup() {
		mdc = mockStatic(MDC.class);
	}
	
	@AfterEach
	void tearDown() {
		mdc.close();
	}
	
	@Test
	void shouldExecuteRunnable() {
		customAsyncTaskExecutor.execute(runnable);
		
		verify(delegate).execute(runnableArgumentCaptor.capture());
		validateWrappedRunnable(runnableArgumentCaptor.getValue());
	}
	
	void validateWrappedRunnable(final Runnable wrappedRunnable) {
		wrappedRunnable.run();
		
		mdc.verify(MDC::clear);
		verify(runnable).run();
	}
	
	@Test
	void shouldSubmitRunnable() {
		customAsyncTaskExecutor.submit(runnable);
		
		verify(delegate).submit(runnableArgumentCaptor.capture());
		validateWrappedRunnable(runnableArgumentCaptor.getValue());
	}
	
	@Test
	void shouldSubmitCallable() throws Exception {
		customAsyncTaskExecutor.submit(callable);
		
		verify(delegate).submit(callableArgumentCaptor.capture());
		validateWrappedCallable(callableArgumentCaptor.getValue());
	}
	
	void validateWrappedCallable(final Callable<String> wrappedCallable) throws Exception {
		final String expectedOutput = "expected";
		when(callable.call()).thenReturn(expectedOutput);
		
		final String result = wrappedCallable.call();
		
		assertEquals(expectedOutput, result);
		mdc.verify(MDC::clear);
		verify(callable).call();
	}
	
	@Test
	void shouldSubmitCompletableRunnable() {
		customAsyncTaskExecutor.submitCompletable(runnable);
		
		verify(delegate).submitCompletable(runnableArgumentCaptor.capture());
		validateWrappedRunnable(runnableArgumentCaptor.getValue());
	}
	
	@Test
	void shouldSubmitCompletableCallable() throws Exception {
		customAsyncTaskExecutor.submitCompletable(callable);
		
		verify(delegate).submitCompletable(callableArgumentCaptor.capture());
		validateWrappedCallable(callableArgumentCaptor.getValue());
	}
}