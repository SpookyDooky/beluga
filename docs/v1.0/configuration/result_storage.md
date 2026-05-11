# Result storage
Results can be stored in either S3 compatible storage or on the filesystem. 
The configuration for either is listed down below. 

# Configuration

## Storage backend

### `beluga.result-datastore.type`
- **Type:** string
- **Required:**: true
- **Default:**: FILE_SYSTEM
- **Allowed values:** FILE_SYSTEM, S3
- **Description:** The type of storage backend to use for result storage

## File system
For the file system nothing has to be configured as the default for `beluga.result-datastore.type` 
is already FILE_SYSTEM.

## S3 compatible
When using S3 compatible storage backends there are a few properties that need to be configured. 
Below is a list of all properties that have to be configured for S3 compatible storage backends.

### `beluga.result-datastore.s3.host`
- **Type:** string
- **Required:**: true
- **Description:** S3 host.

### `beluga.result-datastore.s3.access-key`
- **Type:** string
- **Required:**: true
- **Description:** S3 access key.

### `beluga.result-datastore.s3.secret-key`
- **Type:** string
- **Required:**: true
- **Description:** S3 secret key.

### `beluga.result-datastore.s3.region`
- **Type:** string
- **Required:**: true
- **Description:** AWS region of S3.

### `beluga.result-datastore.s3.bucket`
- **Type:** string
- **Required:**: true
- **Description:** S3 bucket in which to store the results.