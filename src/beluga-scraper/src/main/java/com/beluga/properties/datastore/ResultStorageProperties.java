package com.beluga.properties.datastore;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;

import static com.beluga.properties.datastore.DataStoreType.FILE_SYSTEM;

public class ResultStorageProperties {

	private DataStoreType type = FILE_SYSTEM;

	@Valid
	@Nullable
	private FileSystemProperties fileSystem = new FileSystemProperties();

	@Valid
	@Nullable
	private S3Properties s3;
	
	public DataStoreType getType() {
		return type;
	}
	
	public void setType(final DataStoreType type) {
		this.type = type;

		if (type != FILE_SYSTEM) {
			fileSystem = null;
		}
	}

	public FileSystemProperties getFileSystem() {
		return fileSystem;
	}

	public void setFileSystem(final FileSystemProperties fileSystem) {
		this.fileSystem = fileSystem;
	}

	public S3Properties getS3() {
		return s3;
	}
	
	public void setS3(final S3Properties s3) {
		this.s3 = s3;
	}
}
