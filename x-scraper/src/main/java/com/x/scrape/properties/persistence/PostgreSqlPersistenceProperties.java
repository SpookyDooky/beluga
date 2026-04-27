package com.x.scrape.properties.persistence;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("x-scraper.persistence.postgresql")
public class PostgreSqlPersistenceProperties {
	
	@NotEmpty
	private String url;
	@NotEmpty
	private String username;
	@NotEmpty
	private String password;
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(final String url) {
		this.url = url;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(final String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(final String password) {
		this.password = password;
	}
}
