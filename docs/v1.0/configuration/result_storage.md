# Result storage
Results can be stored in either S3 compatible storage or on the filesystem. 
The configuration for either is listed down below. 

# Contents
* [Configuration](#configuration)
  * [Storage backend](#storage-backend)
    * [beluga.result-storage.type](#belugaresult-storagetype)
  * [File system](#file-system)
    * [beluga.result-storage.file-system.path](#belugaresult-storagefile-systempath)
  * [S3 Compatible](#s3-compatible)
    * [beluga.result-storage.s3.host](#belugaresult-storages3host)
    * [beluga.result-storage.s3.access-key](#belugaresult-storages3access-key)
    * [beluga.result-storage.s3.secret-key](#belugaresult-storages3secret-key)
    * [beluga.result-storage.s3.region](#belugaresult-storages3region)
    * [beluga.result-storage.s3.bucket](#belugaresult-storages3bucket)
    * [beluga.result-storage.s3.prefix](#belugaresult-storages3prefix)
* [Examples](#examples)
  * [S3 Compatible](#s3-compatible-1)

# Configuration

## Storage backend
### `beluga.result-storage.type`
- **Type:** string
- **Required:** true
- **Default:** FILE_SYSTEM
- **Allowed values:** FILE_SYSTEM, S3
- **Description:** The type of storage backend to use for result storage

## File system

### `beluga.result-storage.file-system.path`
- **Type:** string
- **Required:** false
- **Default:** /data/results
- **Description:** Folder in which the results will be stored, this folder needs to be mounted.
## S3 compatible
When using S3 compatible storage backends there are a few properties that need to be configured. 
Below is a list of all properties that have to be configured for S3 compatible storage backends.

### `beluga.result-storage.s3.host`
- **Type:** string
- **Required:** true
- **Description:** S3 host.

### `beluga.result-storage.s3.access-key`
- **Type:** string
- **Required:** true
- **Description:** S3 access key.

### `beluga.result-storage.s3.secret-key`
- **Type:** string
- **Required:** true
- **Description:** S3 secret key.

### `beluga.result-storage.s3.region`
- **Type:** string
- **Required:** true
- **Description:** AWS region of S3.

### `beluga.result-storage.s3.bucket`
- **Type:** string
- **Required:** true
- **Description:** S3 bucket in which to store the results.

### `beluga.result-storage.s3.prefix`
- **Type:** string
- **Required:** false
- **Description:** Prefix under which all files will be stored.

# Examples
Below are some example configurations.

## File system
```yaml
beluga:
  result-storage:
    type: FILE_SYSTEM
    file-system:
      path: /folder/to/mount
```
## S3 Compatible
```yaml
beluga:
  result-storage:
    type: S3
    s3:
      host: s3-host.com
      access-key: accessKey
      secret-key: secretKey
      region: us-east-1
      bucket: beluga-results
```