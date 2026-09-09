# Beluga
Beluga is a general-purpose web scraping engine that lets you define and execute scraping jobs through configuration files
or a REST API. Results can be stored on the filesystem or in S3-compatible storage, with SQLite and PostgreSQL available
for persistent job management. 

## Features
- Configurable web scraping jobs
- REST API for managing scraping jobs
- SQLite and PostgreSQL persistence
- Filesystem and S3-compatible result storage
- Docker support

## Quick Start
Beluga can work with zero configuration by running the following command:
```shell
docker run -p 8080:8080 spookybuster/beluga:v1.0
```

To actually persistent data across restarts the following command can be used:
```shell
docker run -p 8080:8080 -v beluga-data:/app/data spookybuster/beluga:v1.0
```

## Documentation
For the complete documentation, including configuration, scraping jobs,
storage, persistence, API reference, and deployment:

**[Read the documentation →](docs/v1.0/index.md)**

## Roadmap
### V1.1
- [ ] image scraping
- [ ] improve datapoint configuration by introducing datapoint-specific prefixes
- [ ] improve 400 responses with explanations of which field is configured incorrectly

## Contributing
See CONTRIBUTING.md

## Disclaimer
Beluga is provided as an open-source web scraping tool for legitimate purposes. 
Users are responsible for how they use the software and for complying with applicable laws, 
regulations, and the terms of service of websites they interact with.

The project maintainer is not responsible or liable for any misuse, abuse, 
damage, or legal consequences resulting from the use of Beluga.