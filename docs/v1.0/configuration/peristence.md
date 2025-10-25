# Persistence

Persistence is used for persisting job execution and job configuration. This makes it easier to
reuse existing configurations. There are three types of persistence that are currently supported:

- Filesystem
- S3 compatible storage
    - For example minio is S3 compatible but mostly free
- PostgreSQL

## Configuration

To configure which data store should be used for persistence, configuration properties can be used. There
is only one property that should be set that is not specific for a specific data store.
<br>
<br>

| property                   | required | default     | options                     |
|:---------------------------|:---------|:------------|:----------------------------|
| x-scraper.persistence.type | true     | FILE_SYSTEM | FILE_SYSTEM, S3, POSTGRESQL |

### Filesystem

To configure the file system as a data store the following additional properties are required.
<br>
<br>

| property                                 | required | default | example | description                                        |
|:-----------------------------------------|:---------|:--------|:--------|:---------------------------------------------------|
| x-scraper.persistence.file-system.folder | true     | -       | /folder | Configures which folder is used to persist data in |

### S3 compatible storage

To configure an S3 compatible storage as data store for persistence the following properties have to be configured.

| property                           | required | default | example            | description                                                |
|:-----------------------------------|:---------|:--------|:-------------------|:-----------------------------------------------------------|
| x-scraper.persistence.s3.host      | true     | -       | some.host.com      | Host of the object store                                   |
| x-scraper.persistence.s3.accessKey | true     | -       | accessKey          | Public access key to an object store                       |
| x-scraper.persistence.s3.secretKey | true     | -       | secretKey          | Secret key to access some specific part of an object store |
| x-scraper.persistence.s3.bucket    | true     | -       | persistence-bucket | The bucket that should be used for persisting data         | 
| x-scraper.persistence.s3.region    | true     | -       | US_WEST_1          | Region of the object store                                 | 
| x-scraper.persistence.s3.folder    | true     | -       | /persistence       | Folder to use for persisting data                          | 

### PostgreSQL
PostgreSQL is currently not yet supported as a data store for persistence, this is planned for the initial release.