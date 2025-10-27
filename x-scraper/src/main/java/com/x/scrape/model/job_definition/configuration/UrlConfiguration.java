package com.x.scrape.model.job_definition.configuration;

import com.x.scrape.persistence.shared.model.HasId;
import jakarta.persistence.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class UrlConfiguration implements HasId {
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	
	@ElementCollection
	private List<URL> urls = new ArrayList<>();
	private String urlFile;
	
	@Override
	public Long getId() {
		return id;
	}
	
	@Override
	public void setId(final Long id) {
		this.id = id;
	}
	
	public List<URL> getUrls() {
		return urls;
	}
	
	public void setUrls(final List<URL> urls) {
		this.urls = urls;
	}
	
	public String getUrlFile() {
		return urlFile;
	}
	
	public void setUrlFile(final String urlFile) {
		this.urlFile = urlFile;
	}
}
