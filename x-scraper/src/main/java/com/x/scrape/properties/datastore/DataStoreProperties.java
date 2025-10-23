package com.x.scrape.properties.datastore;

import org.springframework.boot.context.properties.ConfigurationProperties;

import static com.x.scrape.properties.datastore.DataStoreType.FILE_SYSTEM;

@ConfigurationProperties("x-scraper.datastore")
public class DataStoreProperties {

	private DataStoreType type = FILE_SYSTEM;
	
	private S3Properties s3;
	
	public DataStoreType getType() {
		return type;
	}
	
	public void setType(final DataStoreType type) {
		this.type = type;
	}
	
	public S3Properties getS3() {
		return s3;
	}
	
	public void setS3(final S3Properties s3) {
		this.s3 = s3;
	}
}
