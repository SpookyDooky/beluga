package com.x.scrape.activity_logging.service;

import com.x.scrape.activity_logging.event.ActivityEvent;
import com.x.scrape.activity_logging.model.ActivityLog;
import com.x.scrape.activity_logging.repository.ActivityLogRepository;
import com.x.scrape.logging.ContextLogger;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This service takes care of collecting all the {@link ActivityEvent}'s. It temporarily stores the {@link ActivityLog}'s
 * in memory before flushing them after x amount of time.
 */
@Service
public class ActivityLoggingService {
	
	private final ContextLogger logger;
	private final ActivityLogMapper activityLogMapper;
	private final ActivityLogRepository repository;
	
	public ActivityLoggingService(final ContextLogger logger,
	                              final ActivityLogMapper activityLogMapper,
	                              final ActivityLogRepository repository) {
		this.logger = logger;
		this.activityLogMapper = activityLogMapper;
		this.repository = repository;
	}
	
	@Async
	@EventListener
	@Transactional
	public void onActivityEvent(final ActivityEvent event) {
		logger.info("Received new activity.");
		final ActivityLog activityLog = activityLogMapper.map(event.getActivity());
		
		repository.save(activityLog);
	}
}
