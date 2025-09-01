package com.x.scrape.properties.scraping;

import com.x.scrape.properties.scraping.storage.FileProperties;
import com.x.scrape.properties.scraping.url.UrlProperties;

public class ScrapingProperties {
	
	private UrlProperties url;
	
	private DataScrapingProperties dataScraping;
	private FileProperties storage;
	
	public UrlProperties getUrl() {
		return url;
	}
	
	public void setUrl(final UrlProperties url) {
		this.url = url;
	}
	
	public DataScrapingProperties getDataScraping() {
		return dataScraping;
	}
	
	public void setDataScraping(final DataScrapingProperties dataScraping) {
		this.dataScraping = dataScraping;
	}
	
	public FileProperties getStorage() {
		return storage;
	}
	
	public void setStorage(final FileProperties storage) {
		this.storage = storage;
	}
}
