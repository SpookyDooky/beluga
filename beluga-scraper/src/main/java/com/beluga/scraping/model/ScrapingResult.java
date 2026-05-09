package com.beluga.scraping.model;

import java.util.List;
import java.util.Map;

public class ScrapingResult {
	
	private final String rawPage;
	private final List<Map<String, Object>> result;
	
	public ScrapingResult(final String rawPage,
	                      final List<Map<String, Object>> result) {
		this.rawPage = rawPage;
		this.result = result;
	}
	
	public String getRawPage() {
		return rawPage;
	}
	
	public List<Map<String, Object>> getResult() {
		return result;
	}
}
