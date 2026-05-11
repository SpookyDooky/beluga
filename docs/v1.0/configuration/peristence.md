# Persistence
Jobs can be persisted in either PostgreSQL or SQLite. The configuration for either is listed down below.

# Contents
* [Configuration](#configuration)
  * [Persistence backend](#persistence-backend)
    * [beluga.persistence.type](#belugapersistencetype)
  * [SQLite](#sqlite)
  * [PostgreSQL](#postgresql)
    * [beluga.persistence.postgresql.url](#belugapersistencepostgresqlurl)
    * [beluga.persistence.postgresql.username](#belugapersistencepostgresqlusername)
    * [beluga.persistence.postgresql.password](#belugapersistencepostgresqlpassword)
* [Example](#examples)
  * [SQLite](#sqlite-1)
  * [PostgreSQL](#postgresql-1)
# Configuration

## Persistence backend

### `beluga.persistence.type`
- **Type:** string
- **Required:** false
- **Default:** SQLITE
- **Allowed values:** SQLITE, POSTGRESQL
- **Description:** Persistence backend used for storing Beluga data.

## SQLite
This is the default, nothing has to be configured for this. The SQLite file is in the following location:
/app/db/sqlite.db. When running Beluga in Docker, it is recommended to mount this directory as a volume to ensure persistence across container restarts.

It is not recommended to use SQLite for production systems, this is mainly offered
to make it run out of the box.

## PostgreSQL

### `beluga.persistence.postgresql.url`
- **Type:** string
- **Required:** true
- **Description:** JDBC URL including the database.

### `beluga.persistence.postgresql.username`
- **Type:** string
- **Required:** true
- **Description:** Username for PostgreSQL

### `beluga.persistence.postgresql.password`
- **Type:** string
- **Required:** true
- **Description:** Password for PostgreSQL

# Examples
Below are some example configurations.

## SQLite
```yaml
beluga:
  persistence:
    type: SQLITE
```
## PostgreSQL
```yaml
beluga:
  persistence:
    type: POSTGRESQL
    postgresql:
      url: jdbc:postgresql://host:1234/database_name
      username: username
      password: password
```