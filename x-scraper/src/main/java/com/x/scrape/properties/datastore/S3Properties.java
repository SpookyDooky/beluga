package com.x.scrape.properties.datastore;

import jakarta.validation.constraints.NotNull;
import software.amazon.awssdk.regions.Region;

public class S3Properties {
	
	@NotNull
	private String host;
	@NotNull
	private String accessKey;
	@NotNull
	private String secretKey;
	@NotNull
	private String bucket;
	
	private Region region;
	
	public String getHost() {
		return host;
	}
	
	public void setHost(final String host) {
		this.host = host;
	}
	
	public String getAccessKey() {
		return accessKey;
	}
	
	public void setAccessKey(final String accessKey) {
		this.accessKey = accessKey;
	}
	
	public String getSecretKey() {
		return secretKey;
	}
	
	public void setSecretKey(final String secretKey) {
		this.secretKey = secretKey;
	}
	
	public String getBucket() {
		return bucket;
	}
	
	public void setBucket(final String bucket) {
		this.bucket = bucket;
	}
	
	public Region getRegion() {
		return region;
	}
	
	public void setRegion(final Region region) {
		this.region = region;
	}
}
