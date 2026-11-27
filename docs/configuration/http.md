# HTTP
Beluga retrieves pages and files with HTTP GET requests. Redirects are followed, and every request sends the
`User-Agent` header `Beluga/1.0`. The properties below control how long a request may take and how large a response
may be. Both are optional; the defaults are used when they are not configured.

# Contents
* [Configuration](#configuration)
  * [beluga.http.timeout](#belugahttptimeout)
  * [beluga.http.max-body-size](#belugahttpmax-body-size)
* [Examples](#examples)

# Configuration

### `beluga.http.timeout`
- **Type:** duration, for example `30s`, `500ms` or `PT1M`. A number without a unit is read as milliseconds.
- **Required:** false
- **Default:** 30s
- **Description:** The maximum time a request may take, covering both connecting and reading the complete response.
  A request that takes longer fails, even when the server keeps sending data slowly. A page that fails this way makes
  its task fail.

### `beluga.http.max-body-size`
- **Type:** data size, for example `10MB` or `512KB`. A number without a unit is read as bytes.
- **Required:** false
- **Default:** 10MB
- **Description:** The maximum size of a response body after decompression. A larger response is rejected rather than
  cut off, so a page that is too large makes its task fail. Must be smaller than 2GB.

# Examples
```yaml
beluga:
  http:
    timeout: 60s
    max-body-size: 20MB
```
