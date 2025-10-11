package com.x.scrape.async;

import org.slf4j.MDC;
import org.springframework.core.task.AsyncTaskExecutor;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class CustomAsyncTaskExecutor implements AsyncTaskExecutor {
	
	private final AsyncTaskExecutor delegate;
	
	public CustomAsyncTaskExecutor(final AsyncTaskExecutor delegate) {
		this.delegate = delegate;
	}
	
	@Override
	public void execute(final Runnable task) {
		delegate.execute(wrap(task));
	}
	
	@Override
	public Future<?> submit(final Runnable task) {
		return delegate.submit(wrap(task));
	}
	
	@Override
	public <T> Future<T> submit(final Callable<T> callable) {
		return delegate.submit(wrap(callable));
	}
	
	@Override
	public CompletableFuture<Void> submitCompletable(final Runnable task) {
		return delegate.submitCompletable(wrap(task));
	}
	
	@Override
	public <T> CompletableFuture<T> submitCompletable(final Callable<T> callable) {
		return delegate.submitCompletable(wrap(callable));
	}
	
	private Runnable wrap(final Runnable runnable) {
		return () -> {
			MDC.clear();
			runnable.run();
		};
	}
	
	private <T> Callable<T> wrap(final Callable<T> callable) {
		return () -> {
			MDC.clear();
			return callable.call();
		};
	}
}
