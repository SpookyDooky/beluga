package com.x.scrape.properties.persistence;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PostgreSqlPersistenceProperties {
	
	@NotBlank
	private String url;
	@NotBlank
	private String username;
	@NotBlank
	@NotNull
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
