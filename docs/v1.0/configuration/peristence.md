# Persistence

Persistence is used for persisting jobDefinition execution and jobDefinition configuration. This makes it easier to
reuse existing configurations. There are three types of persistence that are currently supported:

- PostgreSQL
- SQLite

Note that PostgreSQL is the only persistence type that supports full migrations.

## Configuration

To configure which data store should be used for persistence, configuration properties can be used. There
is only one property that should be set that is not specific for a specific data store.
<br>
<br>

| property                | required | default  | options              |
|:------------------------|:---------|:---------|:---------------------|
| beluga.persistence.type | true     | SQL_LITE | SQL_LITE, POSTGRESQL |

### PostgreSQL

To configure PostgreSQL as persistence data store the following properties have to be configured.

| property                               | required | default | example                              | description  |
|:---------------------------------------|:---------|:--------|:-------------------------------------|:-------------|
| beluga.persistence.postgresql.url      | true     | -       | jdbc:postgresql://host:5432/database | Database url |
| beluga.persistence.postgresql.username | true     | -       | Username                             | -            | 
| beluga.persistence.postgresql.password | true     | -       | Password                             | -            |

### SQL Lite

Per default SQL Lite is included in the docker image, nothing has to be configured for this. The file can be found inside the 
container at /app/db/sqlite.db, do not forget to mount it, otherwise it won't survive restarts. Note that SQLite only makes sure
that the database is correctly created on startup, it does not handle migrations. This means that between versions things can break
when using SQLite. Therefore, it is highly recommended to use PostgreSQL in production environments. 