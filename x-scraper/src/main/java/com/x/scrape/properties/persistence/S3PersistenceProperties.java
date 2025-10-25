package com.x.scrape.properties.persistence;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import software.amazon.awssdk.regions.Region;

/**
 * Configures persistence for a S3 compatible data store.
 */
public class S3PersistenceProperties {
	
	/**
	 * Host of the S3 compatible storage.
	 */
	@NotNull
	private String host;
	
	/**
	 * Public access key for S3 compatible storage.
	 */
	@NotNull
	private String accessKey;
	
	/**
	 * Secret access key for S3 compatible storage.
	 * Should not ever be put in a repository.
	 */
	@NotNull
	private String secretKey;
	
	/**
	 * Bucket that should be used for persisting models.
	 */
	@NotNull
	private String bucket;
	
	/**
	 * Region of S3 compatible storage.
	 */
	@NotNull
	private Region region;
	
	/**
	 * Folder in which to persist the models.
	 */
	@NotEmpty
	private String folder;
	
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
	
	public String getFolder() {
		return folder;
	}
	
	public void setFolder(final String folder) {
		this.folder = folder;
	}
}
