# Result storage
Results can be stored in either S3 compatible storage or on the filesystem. 
The configuration for either is listed down below. 

# Contents
* [Configuration](#configuration)
  * [Storage backend](#storage-backend)
    * [beluga.result-datastore.type](#belugaresult-datastoretype)
  * [File system](#file-system)
  * [S3 Compatible](#s3-compatible)
    * [beluga.result-datastore.s3.host](#belugaresult-datastores3host)
    * [beluga.result-datastore.s3.access-key](#belugaresult-datastores3access-key)
    * [beluga.result-datastore.s3.secret-key](#belugaresult-datastores3secret-key)
    * [beluga.result-datastore.s3.region](#belugaresult-datastores3region)
    * [beluga.result-datastore.s3.bucket](#belugaresult-datastores3bucket)
* [Examples](#examples)
  * [S3 Compatible](#s3-compatible-1)

# Configuration

## Storage backend
### `beluga.result-datastore.type`
- **Type:** string
- **Required:**: true
- **Default:**: FILE_SYSTEM
- **Allowed values:** FILE_SYSTEM, S3
- **Description:** The type of storage backend to use for result storage

## File system
This is the default, nothing has to be configured for this.

## S3 compatible
When using S3 compatible storage backends there are a few properties that need to be configured. 
Below is a list of all properties that have to be configured for S3 compatible storage backends.

### `beluga.result-datastore.s3.host`
- **Type:** string
- **Required:** true
- **Description:** S3 host.

### `beluga.result-datastore.s3.access-key`
- **Type:** string
- **Required:** true
- **Description:** S3 access key.

### `beluga.result-datastore.s3.secret-key`
- **Type:** string
- **Required:** true
- **Description:** S3 secret key.

### `beluga.result-datastore.s3.region`
- **Type:** string
- **Required:** true
- **Description:** AWS region of S3.

### `beluga.result-datastore.s3.bucket`
- **Type:** string
- **Required:** true
- **Description:** S3 bucket in which to store the results.

# Examples
Below are some example configurations.

## S3 Compatible
```yaml
beluga:
  result-datastore:
    type: S3
    s3:
      host: s3-host.com
      access-key: accessKey
      secret-key: secretKey
      region: us-east-1
      bucket: beluga-results
```