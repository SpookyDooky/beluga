# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

Beluga is a general-purpose web scraping engine: a Spring Boot 4 / Java 21 service that manages scraping jobs through a REST API. It persists to SQLite or PostgreSQL and stores results on the filesystem or in S3-compatible storage. User docs and OpenAPI specs are in `docs/v1.0/`; update them when documented behaviour changes.

## Commands

The Maven root is `src/`, not the repository root. Run these from `src/`:

```shell
# Build, including Checkstyle and PMD, without running tests
mvn verify -DskipTests

# Unit tests only (no Docker or /app/db needed)
mvn test -Dtest='!*IntegrationTest' -Dsurefire.failIfNoSpecifiedTests=false

# All tests
mvn test

# Single test class or method
mvn -pl beluga-scraper -am test -Dtest=JitterRateLimiterTest -Dsurefire.failIfNoSpecifiedTests=false
mvn -pl beluga-scraper -am test -Dtest='JobControllerIntegrationTest#shouldCreateJob' -Dsurefire.failIfNoSpecifiedTests=false
```

- `-Dsurefire.failIfNoSpecifiedTests=false` is needed because `-Dtest` also applies to the `logging` module, which has no matching tests.
- Linting is bound to the `verify` phase, so `mvn test` does not run it. Running `pmd:check` directly in the reactor fails because the `logging` jar isn't built yet; use `verify`.

Run from the repository root:

```shell
docker build -t beluga .            # multi-stage build, runs with the docker Spring profile
docker compose -f compose/docker-compose.yml up -d   # local MinIO (9000/9001) and PostgreSQL (5432)
```

## Environment gotchas

- **SQLite path is hardcoded** to `/app/db/sqlite.db` in `SqlitePersistenceConfig`. The directory must exist and be writable, or the Spring context fails to start with `'/app' does not exist`. This affects the SQLite variant of every integration test and any local run using SQLite. On Windows the path resolves to `C:\app\db`, which is separate from `/app` under WSL.
- **Integration tests need Docker**, because the PostgreSQL variant uses Testcontainers.
- **`application.yml` activates the `local` profile.** `application-local.yml` is gitignored, so it may hold machine-specific settings. The Docker image overrides this with the `docker` profile, which imports the file named by `BELUGA_CONFIGURATION_FILE`.
- **Line endings:** the repo stores LF. On a Windows/WSL checkout with CRLF in the working tree, `git status` can list every file as modified. Check real changes with `git diff --ignore-space-at-eol`, and stage specific files rather than using `git add -A`.

## Architecture

The Maven modules are `beluga-scraper` (the application) and `logging` (a small `ContextLogger` wrapper over Log4j2 and MDC).

### Four parallel model layers

The same concepts exist in separate type hierarchies, connected by MapStruct mappers in `mapper/` and `api/*/mapper/`:

| Layer | Package | Examples |
| --- | --- | --- |
| API DTOs | `api/*/dto` | `WriteJobDefinitionDto`, `ReadJobDefinitionDto` |
| YAML properties | `properties/` | `JobProperties` |
| JPA entities | `model/`, `execution/model/task` | `JobDefinition`, `JobExecution`, `TaskDefinition`, `TaskExecution` |
| In-memory runtime | `execution/model` | `Job`, `Task` |

Definitions are the persisted configuration and executions record each run. `Job` and `Task` are non-persistent snapshots built by `JobService`; their IDs are the IDs of the matching `JobExecution` and `TaskExecution`.

Extraction types (`ATTRIBUTE`, `DESCRIPTION_LIST`, `HTML`, `IMAGE`, `TEXT`) are mirrored in each layer. Adding or changing a type means touching the write and read DTOs (including the Jackson subtype registration), properties, entity, runtime configuration, every mapper, `ScrapingService`, and the migrations.

### Execution is event-driven

Components communicate through Spring application events rather than direct calls, so search for `@EventListener` to follow the flow:

1. `ExecutionController` calls `ExecutionApiService`, and `JobService` creates the execution rows and a `Job`.
2. `JobExecutorService.execute` (`@Async`) moves tasks into `JobTaskQueue`, then dispatches them to `Worker` at the rate set by `JitterRateLimiter`. It polls until the job's active-task count reaches zero, then publishes `JobFinishedEvent`.
3. `Worker.execute` (`@Async`) calls `ScrapingService`, which uses jsoup, and publishes task lifecycle events plus two `TaskResultEvent`s (`data.json` and `source.html`).
4. `ResultStorageService` stores each result through `ResultDataStoreProvider` and publishes `TaskResultStoredEvent`.
5. `TaskExecutionEventHandler` and `JobEventHandler` write status changes to the database. `ActivityLoggingService` records `ActivityEvent`s.

Keep these constraints in mind:

- Run state (the queue and active counters) lives in memory in a single instance.
- `@EventListener` methods without `@Async` run synchronously on the publisher's thread. Ordering across `@Async` listeners is not guaranteed.
- `CustomAsyncTaskExecutor` clears the MDC on each async task, so re-establish logging context with `try (CloseableContext ignored = logger.with(...))`.

### Pluggable backends

Backends are selected at startup by properties, using conditional beans:

- `beluga.persistence.type` (`SQLITE` or `POSTGRESQL`) activates `@IsSqlite` or `@IsPostgreSql` configuration. Each config class defines its own data source, entity manager, and transaction manager; the matching Spring Boot auto-configuration is excluded in `BelugaScraper`.
- `beluga.result-storage.type` selects a `ResultDataStoreProvider` implementation (`FILE_SYSTEM` or S3).

When changing persistence:

- Schema changes need a Flyway migration in **both** `db/sqlite/` and `db/postgresql/`. Hibernate DDL generation is off.
- Entity scan packages are hardcoded separately in `SqlitePersistenceConfig` and `PostgreSqlPersistenceConfig`, so a new entity package must be added to both. Only SQLite scans the `InstantConverter` package; this is deliberate, so don't copy it to PostgreSQL.

Removing a task soft-deletes it, and deduplication applies only to active tasks (see `docs/v1.0/system_guarantees/task_deduplication.md`).

## Code style

Checkstyle (`src/checkstyle.xml`) and PMD (`src/pmd-ruleset.xml`) fail the build on violations. PMD enforces its `codestyle` category with a few exclusions, which is why parameters and local variables are declared `final` throughout. Files mix tabs and 4-space indentation; match the file you're editing.

## Testing

- Integration tests extend `BaseIntegrationTest` and must use `@TestTemplate`, not `@Test`. `@MultiStoreTest` runs each test against both PostgreSQL and SQLite, activating the `postgresql` or `sqlite` profile from `src/test/resources`.
- `BaseIntegrationTest` rebuilds the Spring context before every test method, so these tests are slow.
- Surefire matches `*IntegrationTest` classes too; there is no separate Failsafe phase.
- Test data commonly comes from Instancio, with custom generators registered in `src/test/resources/META-INF/services/org.instancio.spi.InstancioServiceProvider`.

## Architectural Invariants

- Job execution state is held in memory.
- A single application instance owns job execution.
- Definitions and executions are separate concepts.
- Schema changes require both SQLite and PostgreSQL migrations.
- Runtime Job and Task instances are non-persistent snapshots.

## Engineering Principles

- Prefer simple, explicit designs over unnecessary abstractions.
- Favor correctness, reliability and observability.
- Avoid introducing dependencies without justification.
- Preserve backward compatibility unless explicitly discussed.
- Write tests for behavioral changes.
- Never silently swallow errors.
- Consider concurrency and resource lifecycle implications.
- Avoid speculative refactoring.
- Do not change public APIs without discussing the impact.
- Run relevant tests before declaring work complete.

## Workflow

- Understand the existing implementation before changing it.
- For non-trivial tasks, propose an implementation plan first.
- Keep changes focused and reasonably scoped.
- Explain important trade-offs.
- Review your own diff before finishing.
- Never claim tests pass without actually running them.

## AI Collaboration Guidelines

- Treat the developer as an experienced backend engineer.
- Do not explain basic programming concepts unless requested.
- Challenge architectural decisions constructively.
- Prefer technical discussions over immediately generating code.
- When investigating issues, trace the actual execution path.
- Always distinguish verified facts from assumptions.
- When proposing changes, explain alternatives and trade-offs.
- Avoid unnecessary hand-holding.
- Never refactor unrelated code while implementing a feature.