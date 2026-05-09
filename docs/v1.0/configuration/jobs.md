# Jobs

Jobs contain the scraping configuration for all tasks that belong to that job. Per job the configuration is the same for
all tasks. Below is a simple example of a full job configuration.

```yaml
beluga:
  jobs:
    - name: job_name
      url:
        url-file: "C:/file_containing_urls.txt"
      storage:
        format: JSON
      execution:
        workers: 1
        tasks-per-second: 1
      scraping:
        element-selector: "repeated#field"
        data-points:
          - selector: "span.something"
            property-name: "regularPrice"
            attribute: content
```
## Configuration
- [Name](#belugajobsname)
- [Urls](#belugajobsurlurl-file)
- [Configuration](#configuration)
- [Storage configuration](#storage-configuration)
- [Execution configuration](#execution-configuration)
- [Scraping configuration](#scraping-configuration)


### `beluga.jobs[].name`
- **Type:** string
- **Required:** true
- **Description:** The name of the job
- **Constraints:** Must be unique across all jobs

### `beluga.jobs[].url.url-file`
- **Type:** string
- **Required:** true
- **Description:** Path to file containing URLs
- **Constraints:** Each URL must be put on a separate line

## Storage configuration

### `beluga.jobs[].storage.format`
- **Type:** string
- **Required:** true
- **Allowed values:** JSON
- **Description:** Format in which the results will be stored

## Execution configuration

### `beluga.jobs[].execution.workers`
- **Type:** integer
- **Required:** false
- **Default:** 1
- **Description:** The amount of workers that are used for a job

### `beluga.jobs[].execution.tasks-per-second`
- **Type:** integer
- **Required:** false
- **Default:** 1
- **Description:** Amount of tasks that are allowed to execute per second

## Scraping configuration

### `beluga.jobs[].scraping.element-selector`
- **Type:** string
- **Required:** true
- **Description:** CSS selector for an element

### `beluga.jobs[].scraping.data-points[].selector`
- **Type:** string
- **Required:** true
- **Description:** CSS selector for which data should be retrieved from an element

### `beluga.jobs[].scraping.data-points[].property-name`
- **Type:** string
- **Required:** true
- **Description:** Name of the property in the results

### `beluga.jobs[].scraping.data-points[].attribute`
- **Type:** string
- **Required:** false
- **Description:** Name of the attribute from which to retrieve the data. When attribute is not specified the text is used.

### `beluga.jobs[].scraping.data-points[].type`
- **Type:** string
- **Required:** false
- **Default:** TEXT
- **Allowed values:** TEXT, IMAGE
- **Description:** Used to specify what type of content to retrieve, if IMAGE is used it will try to retrieve the image from what it retrieved from the attribute.