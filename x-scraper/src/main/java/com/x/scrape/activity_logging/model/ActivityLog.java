package com.x.scrape.activity_logging.model;

import com.x.scrape.activity_logging.activitiy.ActivityType;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.hibernate.annotations.JdbcTypeCode;

import java.time.Instant;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static org.hibernate.type.SqlTypes.JSON;

@Entity
public class ActivityLog {
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	
	private Instant timestamp;
	@Enumerated(STRING)
	private ActivityType type;
	
	@JdbcTypeCode(JSON)
	private ActivityContext context;
	
	public Long getId() {
		return id;
	}
	
	public void setId(final Long id) {
		this.id = id;
	}
	
	public Instant getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(final Instant timestamp) {
		this.timestamp = timestamp;
	}
	
	public ActivityType getType() {
		return type;
	}
	
	public void setType(final ActivityType type) {
		this.type = type;
	}
	
	public ActivityContext getContext() {
		return context;
	}
	
	public void setContext(final ActivityContext context) {
		this.context = context;
	}
}
