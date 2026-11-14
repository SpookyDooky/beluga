# Beluga architecture

The map of this codebase for agents and contributors. Read it before adding a file or module, before changing how two
parts communicate, and before refactoring across package boundaries. Update it in the same change when the structure
changes.

**Conventions in this document**

- Paths are relative to `src/beluga-scraper/src/main/java/com/beluga/` unless stated otherwise. For example, `execution/`
  means `src/beluga-scraper/src/main/java/com/beluga/execution/`.
- Statements are verified against the code unless marked **(inferred)**.
- [Decisions & constraints](#5-decisions--constraints) lists deliberate choices; keep them.
  [Known defects](#6-known-defects) lists real bugs; fix them, never preserve them. Do not confuse the two.
- Last verified against the code on 2026-11-14.

## Contents

1. [Overview](#1-overview)
2. [Folder layout](#2-folder-layout)
3. [Key modules](#3-key-modules)
4. [Data flow](#4-data-flow)
5. [Decisions & constraints](#5-decisions--constraints)
6. [Known defects](#6-known-defects)
7. [Dead code](#7-dead-code)
8. [Testing architecture](#8-testing-architecture)
9. [Change recipes](#9-change-recipes)

---

## 1. Overview

Beluga is a general-purpose web scraping engine: a Spring Boot 4.1 / Java 21 service.

- **Users** define *jobs* through a REST API. YAML `beluga.jobs` exists, but jobs from it are never loaded; see
  [§6](#6-known-defects).
- **A job** is a scraping configuration (an item selector plus extraction rules) and a set of URLs called *tasks*.
- **Starting a job** creates an *execution*. Every active task URL is fetched with jsoup at a rate-limited pace, and the
  configured data points are extracted from each item on the page.
- **Results** (`data.json` and `source.html` per task) are written to the file system or S3-compatible storage.
- **Run state** (queues and counters) lives **in memory in a single instance**.
- **Configuration and history** (definitions, executions, result file index, activity log) live in SQLite or PostgreSQL.

### Main pieces

```
 REST API (api/*)  ──►  services (service/*, api/*/service)  ──►  JPA entities + repositories (model/, persistence/)
        │                                                              ▲
        │ start / stop / pause / resume                                │ status updates (event handlers)
        ▼                                                              │
 JobService ──► runtime Job/Task snapshot ──► JobExecutorService ──► Worker ──► ScrapingService ──► HttpService (jsoup)
                                               (queue + rate limit)    │
                                                                       │ Spring application events
                                                                       ▼
                                   ResultStorageService ──► ResultDataStoreProvider (FILE_SYSTEM | S3)
                                   ActivityLoggingService ──► activity_log table
```

All components after the hand-off to `JobExecutorService` communicate through **Spring application events**, not direct
calls ([§4.3](#43-execution-event-driven)).

### Tech stack

| Concern | Choice |
| --- | --- |
| Framework | Spring Boot 4.1.1 (web MVC, data JPA, validation, Flyway), Java 21 |
| HTML fetching/parsing | jsoup |
| Mapping | MapStruct (componentModel `spring`) |
| JSON | Jackson 3 (`tools.jackson`), built with `JsonMapper.builder().findAndAddModules()` |
| Persistence | Hibernate, with `hibernate-community-dialects` for SQLite; Flyway migrations |
| Object storage | AWS SDK v2 `s3` |
| Rate limiting | Guava `RateLimiter` |
| Logging | Log4j2 via `spring-boot-starter-log4j2`; MDC-based context through the in-house `logging` module |
| Tests | JUnit 5, Mockito, Instancio, AssertJ, Testcontainers (PostgreSQL), OkHttp `mockwebserver3` |
| Lint | Checkstyle (`src/checkstyle.xml`) and PMD (`src/pmd-ruleset.xml`), bound to `verify` |

---

## 2. Folder layout

### Repository root

| Path | Contents |
| --- | --- |
| `src/` | **Maven root** (parent `pom.xml`, `mvnw`, `checkstyle.xml`, `pmd-ruleset.xml`). Run Maven from here. |
| `src/beluga-scraper/` | The application module. |
| `src/logging/` | Small library: `ContextLogger` over Log4j2 plus MDC, auto-configured through `META-INF/spring/...AutoConfiguration.imports`. |
| `docs/` | User docs (`configuration/*.md`, `system_guarantees/`) and OpenAPI specs (`api_specs/*.yaml`). Update them when documented behaviour changes. |
| `compose/docker-compose.yml` | Local MinIO (9000/9001) and PostgreSQL (5432). |
| `Dockerfile` | Multi-stage build. The runtime image creates `/app/db` and `/app/data`, runs as user `beluga` with `SPRING_PROFILES_ACTIVE=docker`, and exposes 8080. |
| `.github/workflows/test.yml` | CI: creates `/app/db`, then runs `mvn verify` (all tests plus lint) from `src/`. |
| `.claude/` | Agent instructions (`CLAUDE.md`, `rules/`, this file). |

### `src/beluga-scraper/src/main/resources`

| Path | Contents |
| --- | --- |
| `application.yml` | Base config. Activates the `local` profile and **sets the defaults** `beluga.persistence.type: SQLITE` and `beluga.result-storage.type: FILE_SYSTEM`. The bean conditions depend on these values; see [§5](#5-decisions--constraints). |
| `application-local.yml` | Gitignored, machine-specific. |
| `application-docker.yml` | Imports `optional:file:${BELUGA_CONFIGURATION_FILE:beluga.yaml}`. |
| `db/sqlite/`, `db/postgresql/` | Flyway migrations, one script per backend: `V1.0.0__beluga_base_line.sql`. |
| `log4j2.xml` | Log pattern; prints the full MDC with `%X`. |

### Java packages (`com.beluga`)

| Package | Responsibility |
| --- | --- |
| `BelugaScraper` | Main class. Excludes `DataSourceAutoConfiguration` and `HibernateJpaAutoConfiguration`, and has `@ConfigurationPropertiesScan`. Flyway auto-configuration is **not** excluded. |
| `api/job` | Job definition CRUD (no delete): controller, read/write/base DTOs, unique-name validation. |
| `api/task` | Task URL management (list, get, PUT replace, PATCH add/remove). |
| `api/execution` | Start, stop, pause and resume, plus execution history. `ExecutionApiService` hands off to the executor. |
| `api/results` | Result retrieval, paged results, file listing and download. |
| `api/exception` | `GlobalExceptionHandler`, `ErrorResponseDtoFactory`, `ErrorResponseDto`. |
| `service/job` | `JobDefinitionService` (repository facade and task add/deactivate), `JobService` (builds executions and runtime snapshots), `JobExecutionService`. |
| `service/task` | `TaskDefinitionService`, `TaskExecutionService`, and `TaskExecutionEventHandler`, which persists task status and `ResultFile` rows from events. |
| `service/results` | `ResultFileService`. |
| `model/job_definition/**` | Definition entities: `JobDefinition`, `ScrapingDefinition`, `ExecutionDefinition`, the `ExtractionDefinition` hierarchy, `ExtractionType`. |
| `model/` (other) | `JobExecution`, `ResultFile`, `JobStatus`, `CompressionType`; `model/event/storable` (payloads). |
| `execution/model/job`, `execution/model/task` | **Runtime** snapshots `Job`, `Task`, `ScrapingConfiguration` and the runtime `ExtractionConfiguration` hierarchy. **Note:** the JPA entities `TaskDefinition` and `TaskExecution` and the enum `TaskStatus` also live in `execution/model/task`. |
| `execution/service/job` | `JobExecutorService` (queue, dispatch and completion), `JobEventHandler` (job status), `JobScheduler` (restart recovery). |
| `execution/service/task` | `JobTaskQueue`. |
| `execution/service/worker` | `Worker` (one task per async call); `rate_limiting/JitterRateLimiter`. |
| `execution/event/**` | Job, task and task-result events ([§4.4](#44-event-catalogue)). |
| `scraping` | `ScrapingService` (extraction) and `model/ScrapingResult`. |
| `http` | `HttpService`: jsoup fetch plus `RequestActivity` events. |
| `result_storage` | `ResultStorageService`, `ResultDataStoreProvider` (abstract), `NamespaceFactory`, `json/` (JSON plus dotted-key nesting), `file_system/`, `s3/`. |
| `activity_logging` | Activity events and the `activity_log` table. The subpackage is spelled **`activitiy`**; don't "fix" it piecemeal. |
| `persistence` | `PersistenceConfig` (`@EnableJpaRepositories`), `sqlite/` and `postgresql/` backend configs, `config/conditionals` (`@IsSqlite`, `@IsPostgreSql`), `repository/`. |
| `properties` | `@ConfigurationProperties("beluga")` tree ([§3.6](#36-configuration-properties)). |
| `mapper` | MapStruct mappers: definitions ↔ DTOs/properties, and definitions → runtime snapshots. |
| `config` | `AsyncConfig` (`@EnableAsync`, `taskExecutor` bean), `SchedulingConfig` (`@EnableScheduling`, currently unused). |
| `async` | `CustomAsyncTaskExecutor` (clears the MDC per task). |
| `util` | `TimingService` (UUID-keyed stopwatches). |

---

## 3. Key modules

### 3.1 Four parallel model layers

The same concepts exist in four separate type hierarchies, connected by MapStruct mappers:

| Layer | Package | Examples | Lifetime |
| --- | --- | --- | --- |
| API DTOs | `api/*/dto` | `WriteJobDefinitionDto`, `ReadJobDefinitionDto`, `Write*/Read*ExtractionConfigurationDto` | Per request |
| YAML properties | `properties/` | `JobProperties`, `ExtractionConfigurationProperties` | Startup |
| JPA entities | `model/`, `execution/model/task` | `JobDefinition`, `JobExecution`, `TaskDefinition`, `TaskExecution`, `*ExtractionDefinition` | Persisted |
| Runtime snapshot | `execution/model` | `Job`, `Task`, `*ExtractionConfiguration` | One execution run, in memory |

**Extraction types** (`ATTRIBUTE`, `DESCRIPTION_LIST`, `HTML`, `IMAGE`, `TEXT`) are mirrored in every layer, and each layer
dispatches on type differently:

| Layer | Type representation | Dispatch |
| --- | --- | --- |
| Write DTO | `"type"` JSON property, `@JsonTypeInfo(NAME)` + `@JsonSubTypes` on `WriteExtractionConfigurationDto` | Jackson |
| Properties | One nested block per type (`text:`, `attribute:`, …) on `ExtractionConfigurationProperties`; **no `type` key** | Sealed interface `ExtractionTypeProperties` + exhaustive pattern `switch` in `ExtractionDefinitionMapperService` |
| Entity | `@Inheritance(JOINED)`, `@DiscriminatorColumn("type")`, read-only `type` field | Hibernate |
| DTO ↔ entity | — | MapStruct `@SubclassMapping`, `subclassExhaustiveStrategy = RUNTIME_EXCEPTION` (`ExtractionDefinitionMapper`) |
| Entity → runtime | — | MapStruct `@SubclassMapping` (`mapper/task/configuration/extraction_configuration/ExtractionConfigurationMapper`) |
| Runtime | Plain abstract `ExtractionConfiguration` (not sealed) | Pattern `switch` with `default -> IllegalArgumentException` in `ScrapingService` |

Fields per type:

| Type | Fields |
| --- | --- |
| ATTRIBUTE | `selector`, `field`, `attribute` |
| DESCRIPTION_LIST | `selector`, `dataPoints[]` of `{dtValue, field}` (non-empty). **No top-level `field`.** |
| HTML | `selector`, `field` |
| TEXT | `selector`, `field` |
| IMAGE | none. A no-op at runtime ([§3.4](#34-scraping)). |

### 3.2 Domain / persistence model

**Definitions vs executions.** These are separate concepts and must stay separate.

- **Definitions** are mutable configuration: `JobDefinition`, `TaskDefinition`, `ScrapingDefinition`,
  `ExecutionDefinition`, `ExtractionDefinition`.
- **Executions** record one run each: `JobExecution`, `TaskExecution`, `ResultFile`. They point back at the definitions.

```
JobDefinition ─1:1 (cascade ALL)─ ScrapingDefinition ─1:N (cascade ALL, orphanRemoval)─ ExtractionDefinition (JOINED)
      │        └1:1 (cascade ALL)─ ExecutionDefinition {tasksPerSecond: double}        └─ DescriptionList ─1:N─ DataPointDefinition
      ├─1:N (cascade ALL)─ TaskDefinition {url: java.net.URL, active}
      └─1:N (cascade ALL, @OrderBy executedAt DESC)─ JobExecution {executedAt, status: JobStatus}
                                                          └─1:N─ TaskExecution {executedAt, status: TaskStatus} ──N:1──► TaskDefinition
                                                                       └─1:N─ ResultFile {namespace, key, sizeInBytes, compressionType}
ActivityLog {timestamp, type, context: JSON}   (standalone)
```

**Key behaviours**

- **Latest execution:** "most recent execution" is `JobDefinition.executions.getFirst()`. `addExecution` uses `addFirst`
  to keep it consistent with `@OrderBy executedAt DESC`.
- **`JobExecution.setTasks` appends**; it does not replace.
- **Soft delete:** removing a task sets `TaskDefinition.active=false`. Rows are never deleted, because `TaskExecution`
  references them.
- **Deduplication** applies only among *active* tasks, and it is enforced **only in application code** (`Set<URL>`
  semantics plus active-URL filters). No DB constraint backs it. See `docs/system_guarantees/task_deduplication.md` and
  [§6](#6-known-defects).
- **`ResultFile`** stores `namespace` and `key` separately. Its resource identifier is `namespace + "/" + key`. Retrieval
  uses the stored values, so it doesn't depend on the current configuration.
- **Extraction ids on update:** `PUT /jobs/{id}` **replaces** the extraction list (`ScrapingDefinitionMapper.update`
  uses `TARGET_IMMUTABLE` plus orphanRemoval), so extraction ids change on every update.

**Statuses**

- `JobStatus`: `PLANNED, PAUSED, STOPPED, ACTIVE, COMPLETED`.
- `TaskStatus`: `PLANNED, ACTIVE, COMPLETED, FAILED, STOPPED, PAUSED`.
- No transition guards exist; any status can be written over any other.

| Transition | Where |
| --- | --- |
| Job → PLANNED (created) | `JobService.createJobByJobDefinitionId` |
| Job PLANNED → ACTIVE | `JobEventHandler#onJobStartedEvent` |
| Job → COMPLETED | `JobEventHandler#onJobFinishedEvent`. This happens **even if every task failed**. |
| Job → STOPPED / PAUSED | `ExecutionApiService.stop/pause`, ignored if COMPLETED |
| Job PAUSED → ACTIVE | `ExecutionApiService.resume` |
| Task → ACTIVE (+ `executedAt`) / COMPLETED / FAILED | `TaskExecutionEventHandler` |
| Task → STOPPED / PAUSED (queued tasks only) | `JobExecutorService.stop/pause` → `TaskExecutionService.setStatusById` |

**Transactions**

- Only Spring's `@Transactional` is used, never `jakarta.transaction.Transactional`.
- `JobController` and `TaskController` put `@Transactional` on handler methods. `ExecutionController` and
  `ResultsController` rely on transactional services.
- Many read helpers use `propagation = MANDATORY`, so they **must** be called inside a transaction. This includes
  `JobDefinitionService.getById/findById`, `TaskExecutionService.*`, `ResultFileService.*`, `JobService.createActiveJob`
  and `TaskResultDtoMapper.map`.
- Exactly one `transactionManager` bean exists, from whichever backend config is active.

### 3.3 Persistence backends

- **Selection:** `beluga.persistence.type` is read **raw from the `Environment`** by `IsSqliteCondition` and
  `IsPostgreSqlCondition`, with exact, case-sensitive `equals` against `SQLITE` or `POSTGRESQL`.
- **Shared beans:** each config class defines the same bean names: `dataSource` (Hikari), `jpaVendorAdapter`,
  `entityManagerFactory`, `transactionManager`, `flywayCustomizer`.
- **Shared Hibernate settings:** both use `CamelCaseToUnderscoresNamingStrategy` with DDL generation off.

| | SQLite (`persistence/sqlite/config/SqlitePersistenceConfig`) | PostgreSQL (`persistence/postgresql/config/PostgreSqlPersistenceConfig`) |
| --- | --- | --- |
| JDBC URL | **Hardcoded** `jdbc:sqlite:/app/db/sqlite.db?foreign_keys=on` | `beluga.persistence.postgresql.url/username/password` |
| Pool | `maximumPoolSize(1)`, so all DB work is serialized | Hikari defaults |
| Dialect | `org.hibernate.community.dialect.SQLiteDialect` | `PostgreSQLDialect` |
| Flyway | `classpath:db/sqlite`, no schemas | `classpath:db/postgresql`, `createSchemas(true)`, schema `beluga` |
| Extra | `InstantConverter` (`@Converter(autoApply=true)`) stores `Instant` as ISO-8601 text; also registered as a bean | Hibernate sets no `default_schema`, so the JDBC URL needs `currentSchema=beluga` **(inferred)** |

**Entity scan packages** are hardcoded **separately in both configs**:

- **Both:** `com.beluga.model`, `com.beluga.execution.model`, `com.beluga.activity_logging.model`,
  `com.beluga.persistence.repository`.
- **SQLite only:** `com.beluga.persistence.sqlite.converter` (deliberate; see [§5](#5-decisions--constraints)).

**Repositories** (`persistence/repository`)

- `JobDefinitionRepository`: `existsByName`, `deleteByName`.
- `JobExecutionRepository`: `findByStatusIn`.
- `TaskExecutionRepository`: `findByJobExecution(Pageable)`, `findByJobExecutionAndStatusIn`.
- `ResultFileRepository`: `findByTaskExecutionAndKey`, `findByTaskExecution_JobExecutionAndKey(Pageable)`.
- `TaskDefinitionRepository`: four **native** queries over `task_definition`, covering active-URL lookup and
  deactivation.

**Migrations:** a single baseline `V1.0.0__beluga_base_line.sql` per backend.

- **Tables:** `execution_definition`, `scraping_definition`, `job_definition` (name UNIQUE), `job_execution`,
  `task_definition`, `task_execution`, `result_file`, `activity_log`, `extraction_definition`, plus one table per
  subtype and `description_list_extraction_data_point_definition`.
- **Indexes in both:** `task_execution(job_execution_id)`, `result_file(task_execution_id)`, `result_file(key)`.
- **Differences between the backends:**

  | | SQLite | PostgreSQL |
  | --- | --- | --- |
  | Ids | `INTEGER PRIMARY KEY` | `bigint generated always as identity` |
  | Timestamps | `text` | `timestamp` |
  | Booleans | `integer` | `bool` |
  | `result_file.key` | nullable | `not null` |
  | Constraint names | unnamed | named |

### 3.4 Scraping

- **`HttpService.retrievePageAsDocument`** calls `Jsoup.connect(url).timeout(30_000).get()`. There is no custom user
  agent, headers or `ignoreHttpErrors`, so non-2xx responses and non-HTML responses throw **(inferred from jsoup
  defaults)**. Exceptions are logged and become `Optional.empty`. Every request publishes a `RequestActivity` with its
  duration and success flag.
- **`ScrapingService.scrape(url, scrapingConfiguration)`:**
  - An empty fetch throws `IllegalStateException`, which makes the task FAILED.
  - Items are `document.body().select(itemSelector)`; only the body is searched.
  - Each item becomes one `Map<String,Object>`. Each extraction's map is `putAll`-ed into it, so **later fields with
    the same name overwrite earlier ones**.
  - Output is `ScrapingResult(document.toString(), List<Map>)`. The raw page is jsoup's **re-serialized** document, not
    the original bytes.
- **Per type:**

  | Type | Behaviour |
  | --- | --- |
  | TEXT | `element.select(selector).text()`: combined text of **all** matches. |
  | HTML | `element.select(selector).html()`: inner HTML of all matches, joined. |
  | ATTRIBUTE | `element.select(selector).attr(attribute)`: the first match that has the attribute, or `""`. |
  | DESCRIPTION_LIST | See the steps below. |
  | IMAGE | **No-op**; returns `Map.of()`. Planned for v1.1. Nothing produces an `ImagePayload` today. |

  DESCRIPTION_LIST works like this:
  1. If the selector matches more than one element, it throws and the task FAILS.
  2. If it matches nothing, it returns `null` and the fields are absent.
  3. Otherwise it walks the matched element's **direct children**. A `dt` whose `text()` equals `dtValue` exactly
     (case-sensitive) marks a match.
  4. The text of the **next `dd`** after the match is returned.
  5. A configured data point with no match is stored as JSON `null`.

  `dt`/`dd` pairs wrapped in a `<div>` are not found.

- **Output per task:** `Worker` publishes two `TaskResultEvent`s, with key `data.json` (`JsonPayload` of the item list)
  and key `source.html` (`StringPayload` of the page).

### 3.5 Result storage

- **`ResultStorageService#onTaskResultEvent`** (`@EventListener @Async`) works as follows:
  1. It builds the namespace with `NamespaceFactory`.
  2. It converts the payload by type:
     - `JsonPayload`: `JsonService.toJson`.
     - `StringPayload`: raw bytes.
     - `ImagePayload`: the stream.
     - Anything else throws `IllegalStateException`.
  3. It saves through the single active `ResultDataStoreProvider`, then publishes `TaskResultStoredEvent`.
- **Providers** are selected with `@ConditionalOnProperty(name = "beluga.result-storage.type", havingValue = ...)`, with
  no `matchIfMissing`:
  - `FILE_SYSTEM`: `FileSystemResultDataStoreProvider` writes to `Path.of(identifier)` after `mkdirs`.
  - `S3`: `S3Config` (bean `results-s3client`: static credentials, endpoint override, `forcePathStyle(true)`),
    `S3Service` and `S3ResultDataStoreProvider`.
- **Namespace layout:** `{prefix}/{jobDefinitionId}/{jobExecutionId}/{taskExecutionId}`. Each file is
  `{namespace}/{key}`, for example `/app/data/3/17/942/data.json`.
  - The file-system prefix is **hardcoded** to `/app/data`. There is no `file-system.path` property; it is silently
    ignored if set.
  - The S3 prefix is `beluga.result-storage.s3.prefix` (default `""`, so keys start with `/`).
- **JSON nesting** (`JsonNestingService`): for a `Collection` payload, the dotted keys of each `Map` element are
  expanded into nested objects, so `{"a.b.c":1}` becomes `{"a":{"b":{"c":1}}}`. Non-collection payloads are serialized
  as-is.
- **Retrieval:** `ResultStorageService.retrieve(identifier)` delegates to the provider. `TaskResultDtoMapper` parses
  `data.json` back into a `List`.

### 3.6 Configuration properties

`BelugaScraperProperties` (`@ConfigurationProperties("beluga")`, `@Validated`):

```
beluga
├── persistence            PersistenceProperties  @HasCorrectPersistenceStore @HasOnlyOnePersistenceStore
│   ├── type               SQLITE | POSTGRESQL
│   └── postgresql         url, username, password (@NotBlank)
├── result-storage         ResultStorageProperties
│   ├── type               FILE_SYSTEM | S3
│   └── s3                 host, access-key, secret-key, bucket, region (@NotNull), prefix
└── jobs[]                 JobProperties          (bound and validated, but NEVER loaded; see §6)
    ├── name               @NotBlank
    ├── url                urls[] | url-file      (urls wins when non-empty)
    ├── execution          tasks-per-second (int, default 1)
    └── scraping           item-selector (@NotBlank), data-points[] → ExtractionConfigurationProperties
                           exactly one of: text | html | attribute | image | description-list
                           (@HasExactlyOneExtractionConfiguration; per-type @NotBlank/@NotEmpty)
```

- **Defaults:** Java field defaults (`type = SQLITE`, `type = FILE_SYSTEM`) don't drive the backend conditions. The
  `application.yml` defaults do.
- **Unknown keys:** binding uses Spring's default `ignoreUnknownFields=true`, so misspelled keys inside a bound object
  are silently ignored.

### 3.7 Logging module (`src/logging`)

- **`LoggingConfig`** provides a **prototype** `ContextLogger` bean per injection point, named after the injecting class.
- **Usage:** inject `ContextLogger` and scope the context with
  `try (CloseableContext ignored = logger.with(...)) { ... }`. Use the keys in `ContextKeys`.
- **`with` variants:** `with(String... keyValuePairs)` (pairs only; an odd trailing key is dropped), `with(ContextLoggable)`
  (implemented by `Task` and `TaskEvent`), `withKey(k, v)` and `withKeys(Map)`.
- **Log methods:** only `info`, `error` and `trace` exist.
- **Async threads:** `CustomAsyncTaskExecutor` clears the MDC **before every async task**. Context is never propagated
  across `@Async` boundaries, so re-establish it at the start of every async method.
- **Exceptions:** `ScrapingService` uses a plain Log4j `Logger`.

Example (`Worker`):

```java
try (final CloseableContext ignored = logger.with(JOB_EXECUTION_ID, task.getJob().getId().toString())) {
    executeTask(task);   // inside: try (CloseableContext ignored = logger.with(task)) { ... }
}
```

### 3.8 Activity logging

- **`ActivityEvent(Activity)`** is handled by `ActivityLoggingService#onActivityEvent` (`@Async @Transactional`), which
  saves one `ActivityLog` row immediately. The class javadoc mentions buffering; that is stale.
- **Activities** (`ActivityType`):

  | Activity | Fields | Published by |
  | --- | --- | --- |
  | `RequestActivity` | url, duration, success | `HttpService` |
  | `TaskStartedActivity` | url | `Worker` |
  | `TaskCompletedActivity` | url, duration | `Worker` |
  | `TaskFailedActivity` | — | **never published** |

- **Storage:** `ActivityLogMapper` converts the activity to a `Map` with Jackson, removes `timestamp` and `type`, and
  stores the rest in the JSON `context` column.

---

## 4. Data flow

### 4.1 REST API

| Method | Path | Handler | Notes |
| --- | --- | --- | --- |
| POST | `/jobs` | `JobController#create` | Returns **200**, not 201. 400 on validation failure or duplicate name. |
| GET | `/jobs/{id}` | `JobController#get` | 404 if missing. The response has **no `name`**. |
| PUT | `/jobs/{id}` | `JobController#update` | Replaces config; extraction ids change. |
| GET | `/jobs/{jobDefinitionId}/tasks` | `TaskController#getTasks` | Active tasks only. |
| GET | `/jobs/{jobDefinitionId}/tasks/{taskId}` | `TaskController#getTask` | `taskId` = **TaskDefinition** id. Also returns inactive tasks. |
| PUT | `/jobs/{jobDefinitionId}/tasks` | `TaskController#updateTasks(UpdateTaskDto)` | `{urls:[...]}` replaces the active set. |
| PATCH | `/jobs/{jobDefinitionId}/tasks` | `TaskController#updateTasks(PatchTaskDto)` | `{add:[...], remove:[...]}` |
| POST | `/jobs/{jobDefinitionId}/start` \| `stop` \| `pause` \| `resume` | `ExecutionController` | 204, or 404 if the job is missing. |
| GET | `/jobs/{jobDefinitionId}/executions` \| `/latest` \| `/{executionId}` | `ExecutionController` | List is newest first. `/{executionId}` includes tasks. |
| GET | `/jobs/{jobId}/executions/{executionId}/results?page&size` | `ResultsController` | `Page<TaskResultDto>`, unsorted. |
| GET | `.../executions/{executionId}/tasks/{taskId}/results` | `ResultsController` | `taskId` = **TaskExecution** id. |
| GET | `.../tasks/{taskId}/results/files` | `ResultsController` | File metadata. |
| GET | `.../tasks/{taskId}/results/files/download?fileName=` | `ResultsController` | Raw bytes. `fileName` must match a stored key exactly (`data.json`, `source.html`). |

**`taskId` has two meanings:** it is a TaskDefinition id under `/tasks` and a TaskExecution id under the results routes.

**Error handling**

- **`GlobalExceptionHandler`:**
  - `MethodArgumentNotValidException` → 400 `{timestamp, status, errors:[defaultMessage...]}`. Field names are dropped.
  - **Any** `IllegalArgumentException` → 400 `{timestamp, status, errors:[message]}`.
- **Controller-local `@ExceptionHandler`s** return 404 with an empty body:
  - `ExecutionController`: `JobDefinitionNotFoundException`.
  - `TaskController`: `JobDefinitionNotFoundException`, `TaskDefinitionNotFoundException`.
  - `ResultsController`: `TaskResultNotFoundException`, `EntityNotFoundException`.
- **Everything else** falls to Spring Boot's default error handling (500) **(inferred)**.

### 4.2 Request flows (before the executor)

**Create job.** `JobController.create` (`@Transactional`):

1. Bean validation runs on `@Valid`, but it is shallow; see [§6](#6-known-defects).
2. `JobNameValidator` checks `existsByName`, and a duplicate throws `IllegalArgumentException`, which becomes 400. The
   DB unique constraint is the backstop.
3. `JobDefinitionMapper.map(dto)` maps the DTO and saves it, cascading.
4. The saved entity is mapped to `ReadJobDefinitionDto`.

**PUT tasks (replace).** `TaskController`:

1. A native query finds which requested URLs are already active.
2. A native `@Modifying` update sets every *other* active task to `active=false` (soft delete).
3. New URLs become new `TaskDefinition`s (cascade save).

Kept URLs keep their ids.

**PATCH tasks.**

1. `remove` URLs are deactivated in memory and saved.
2. `add` URLs are filtered against the active set with a native query, and the rest are added.

**Start.** `ExecutionApiService.start` (`@Transactional`):

1. If `hasRunningJob` (latest execution ACTIVE or PAUSED), it does nothing.
2. Otherwise `JobService.createJobByJobDefinitionId`:
   - creates `JobExecution(PLANNED)` and saves it to get its id;
   - creates one `TaskExecution` per **active** `TaskDefinition`;
   - builds the runtime `Job` and `Task`s with `JobMapper`/`TaskMapper`, setting `Job.id` to the JobExecution id and
     each `Task.id` to its TaskExecution id.
3. A `TransactionSynchronization.afterCommit` calls `JobExecutorService.execute(job)`, so the async thread sees the
   committed rows.

**Resume** calls `execute` **inside** the transaction, not `afterCommit`; see [§6](#6-known-defects).

**Startup recovery.** `JobScheduler.startOutstandingJobs` (`@Async`, on `ApplicationReadyEvent`) re-runs every execution
in PLANNED or ACTIVE, using `JobService.createActiveJob`. That includes its PLANNED, PAUSED and ACTIVE tasks, so tasks
that were in flight are scraped again. PAUSED executions are left alone. A job with no tasks still runs, so that it
reaches COMPLETED.

### 4.3 Execution (event-driven)

| # | Step | Thread |
| --- | --- | --- |
| 1 | `JobExecutorService.execute(Job)` (`@Async`) offers all tasks to `JobTaskQueue`, clears `job.tasks`, registers the job in `jobIdJobMap`, and sets `jobActiveTasks[id] = 0`. | async (holds one pool thread for the whole job) |
| 2 | Publishes `JobStartedEvent` → `JobEventHandler` sets the execution to ACTIVE. | same, synchronous |
| 3 | Creates a `JitterRateLimiter(tasksPerSecond)`. While the queue isn't empty: `acquire()`, `pollTask`, increment the counter, call `Worker.execute(task)`. | same |
| 4 | `Worker.execute` (`@Async`) establishes the MDC, then publishes `TaskStartedEvent` → `TaskExecutionEventHandler#onTaskStarted` (ACTIVE plus `executedAt`), and an `ActivityEvent(TaskStartedActivity)`. | async worker; status update is synchronous and transactional |
| 5 | `ScrapingService.scrape` → `HttpService` (publishes `RequestActivity`). | worker |
| 6 | Publishes `TaskResultEvent` for `data.json` and for `source.html` → `ResultStorageService` (`@Async`) → provider → `TaskResultStoredEvent` → `TaskExecutionEventHandler#onTaskResultStored` (`@Async`) inserts a `ResultFile`. | async |
| 7 | Publishes `ActivityEvent(TaskCompletedActivity)`, then `TaskCompletedEvent` → `TaskExecutionEventHandler#onTaskCompleted` (synchronous, COMPLETED) and `JobExecutorService#onTaskCompletedEvent` (`@Async`, decrements the counter). | worker + async |
| 7' | **On any exception in 4–7:** the error is logged, then `TaskFailedEvent` → `onTaskFailed` (synchronous, FAILED) and `JobExecutorService#onTaskFailedEvent` (`@Async`, decrements the counter). No result events and no failure activity. | worker |
| 8 | After the queue drains, `waitForAllTasksToFinish` polls the counter every **100 ms** until it reaches 0. | job thread |
| 9 | Unless the job id is in `interruptedJobs`, publishes `JobFinishedEvent` → `JobEventHandler` sets COMPLETED. It then removes the job from `jobIdJobMap`, `jobActiveTasks` and `interruptedJobs`. | job thread, synchronous |

**Consequences of the async storage step**

- A task, and even the whole job, can be COMPLETED **before** its results are stored. The results endpoints can briefly
  return 404 for a fresh task.
- A storage failure is only logged by Spring's default async exception handler **(inferred)**. It never fails the task,
  and no `ResultFile` row is written.

**Pause / stop / resume**

- **Pause:** `ExecutionApiService.pause` sets the execution to PAUSED, then `JobExecutorService.pause` does three things:
  - marks the job as interrupted, only if it is running;
  - removes its queue;
  - sets the queued tasks to PAUSED.

  In-flight tasks finish normally. The dispatch loop exits because a missing queue counts as empty, and the waiter
  suppresses `JobFinishedEvent`.
- **Stop:** the same as pause, with STOPPED instead.
- **Resume:** only acts on a PAUSED latest execution. It sets ACTIVE, builds a job from the PAUSED tasks, and calls
  `execute` with the **same** JobExecution id.

### 4.4 Event catalogue

| Event | Fields | Published by | Listeners | Mode |
| --- | --- | --- | --- | --- |
| `JobStartedEvent` | jobDefinitionId, jobExecutionId | `JobExecutorService#execute` | `JobEventHandler#onJobStartedEvent` | sync |
| `JobFinishedEvent` | jobDefinitionId, jobExecutionId | `JobExecutorService#waitForAllTasksToFinish` | `JobEventHandler#onJobFinishedEvent` | sync |
| `TaskStartedEvent` | jobDefinitionId, jobId (= JobExecution id), taskId (= TaskExecution id) | `Worker` | `TaskExecutionEventHandler#onTaskStarted` | sync, tx |
| `TaskCompletedEvent` | same as above | `Worker` | `TaskExecutionEventHandler#onTaskCompleted` (sync, tx); `JobExecutorService#onTaskCompletedEvent` (async) | mixed |
| `TaskFailedEvent` | same as above | `Worker` | `TaskExecutionEventHandler#onTaskFailed` (sync, tx); `JobExecutorService#onTaskFailedEvent` (async) | mixed |
| `TaskResultEvent` | + key, `Payload<?>` | `Worker` | `ResultStorageService#onTaskResultEvent` | async |
| `TaskResultStoredEvent` | + namespace, key, payload | `ResultStorageService` | `TaskExecutionEventHandler#onTaskResultStored` | async |
| `ActivityEvent` | `Activity` | `Worker`, `HttpService` | `ActivityLoggingService#onActivityEvent` | async, tx |
| `ApplicationReadyEvent` (Spring) | — | Spring Boot | `JobScheduler#startOutstandingJobs` | async |

**Listener ordering.** Listeners without `@Async` run on the publisher's thread, in an order the code doesn't define. The
order across `@Async` listeners is not guaranteed.

### 4.5 Concurrency and in-memory state

| State | Type | Owner |
| --- | --- | --- |
| Per-job task queue | `ConcurrentHashMap<Long, ConcurrentLinkedQueue<Task>>` | `JobTaskQueue` |
| Running jobs | `HashMap<Long, Job>` (**not thread-safe**) | `JobExecutorService.jobIdJobMap` |
| Active-task counters | `HashMap<Long, AtomicInteger>` (map **not thread-safe**) | `JobExecutorService.jobActiveTasks` |
| Interrupted jobs | concurrent key set | `JobExecutorService.interruptedJobs` |

- **Single executor:** `AsyncConfig.taskExecutor` is a `ThreadPoolTaskExecutor` with core 256, **queue capacity 0** and
  the default (unbounded) max pool size, wrapped in `CustomAsyncTaskExecutor`. **Every** `@Async` method shares this
  pool: job loops, workers, storage, activity and counter listeners.
- **Concurrency limit:** there is no cap on concurrent workers. Effective concurrency is roughly rate × task duration.
  `ExecutionConfiguration.workers` exists but is never read.
- **`JitterRateLimiter`:** Guava `RateLimiter.create(tasksPerSecond)`.
  - After each permit it sleeps a random jitter in `[min(2000, 0.3·base), min(5000, 0.7·base))` ms, where
    `base = 1000 / rate`.
  - With 5 % probability it adds a further sleep; see [§6](#6-known-defects).
  - The average rate stays about `tasksPerSecond`; only the phase shifts **(inferred)**.
- **Shutdown:** not handled. There is no `@PreDestroy`, `SmartLifecycle` or `ContextClosedEvent`. In-flight runs are
  lost, and recovery happens on the next startup through `JobScheduler`.

---

## 5. Decisions & constraints

These look odd but are deliberate. **Don't "fix" them** without discussing it first.

### Architectural invariants

1. **One instance owns execution.** Job execution state lives in memory, so horizontal scaling isn't supported. Don't
   add a second instance or move queue state without a design change.
2. **Definitions and executions are separate concepts.** Never store run state on a definition, and never mutate a
   definition from the executor.
3. **Runtime `Job` and `Task` are non-persistent snapshots.** Their ids are the `JobExecution` and `TaskExecution` ids,
   not definition ids. `JobMapper` auto-maps `id ← definition.id`, and `JobService` deliberately overwrites it.
4. **Components after the executor hand-off communicate through Spring events.** Follow the flow by searching for
   `@EventListener`. Don't add direct calls between the execution, storage, status and activity components.
5. **Schema changes need a Flyway migration in both `db/sqlite/` and `db/postgresql/`.** Hibernate DDL generation is off.

### Deliberate choices

- **Start hands off `afterCommit`,** so the async executor never reads uncommitted execution rows.
- **`interruptedJobs` is only set for running jobs,** so a stale mark can't suppress a later `JobFinishedEvent`.
- **An execution with zero tasks still runs through the executor,** so it reaches COMPLETED.
- **`job.tasks` is cleared after enqueueing.** The queue is the source of truth while running.
- **Rate limiting is per job, not per host.** Each running job gets its own `JitterRateLimiter(tasksPerSecond)`, so
  several jobs that target the same site add up their rates. This is intended. Per-host limiting is a possible future
  feature, not a missing one; don't flag it as a defect.
- **The MDC is cleared on every async task** (`CustomAsyncTaskExecutor`), so context never leaks between pooled tasks.
  Re-establish it with `logger.with(...)`.
- **Only SQLite scans `com.beluga.persistence.sqlite.converter` (`InstantConverter`).** SQLite has no timestamp type, so
  `Instant` is stored as ISO-8601 text. PostgreSQL uses native `timestamp`. Don't copy the converter to PostgreSQL.
- **Entity scan packages are listed separately** in `SqlitePersistenceConfig` and `PostgreSqlPersistenceConfig`. A new
  entity package must be added to **both**.
- **Each backend config defines its own** data source, entity manager and transaction manager. The matching Boot
  auto-configurations are excluded in `BelugaScraper`.
- **SQLite runs with a single connection** (`maximumPoolSize(1)`) to avoid `SQLITE_BUSY` **(inferred)**. All DB work is
  serialized on SQLite.
- **The backend defaults live in `application.yml`,** not in the Java field defaults, because the conditional beans read
  the raw `Environment`. Removing those YAML defaults means no persistence config and no storage provider get created.
- **Task deduplication applies to active tasks only; removal is a soft delete,** so history (`TaskExecution`) keeps a
  valid reference. See `docs/system_guarantees/task_deduplication.md`.
- **Properties data points use one block per type with no `type` key,** so a type and its settings can't disagree.
  Exactly one block is required. The DTOs keep a `type` discriminator because that is the JSON API's contract.
- **`ExtractionTypeProperties` is sealed,** and its implementations must share its package. A sealed interface in the
  unnamed module requires that. In return, `ExtractionDefinitionMapperService` gets a compile-time exhaustiveness check.
- **Hardcoded runtime paths:** `/app/db/sqlite.db` and `/app/data` match the Docker image layout. On Windows they
  resolve to `C:\app\...`, which is separate from `/app` under WSL.

---

## 6. Testing architecture

- **Test layout:** `src/beluga-scraper/src/test/java`, mirroring the main packages. Surefire also runs
  `*IntegrationTest`; there is no Failsafe phase.
- **Unit tests:**
  - Mockito plus Instancio. Custom Instancio generators are registered in
    `src/test/resources/META-INF/services/org.instancio.spi.InstancioServiceProvider`.
  - Configuration binding and bean wiring are tested with `ApplicationContextRunner`, which is fast and needs no
    Docker. See `BelugaScraperPropertiesTest` and `ResultStorageBackendSelectionTest`. Prefer this over an integration
    test for property, conditional-bean or wiring checks.
- **Integration tests** (`integration_test/`):
  - They extend `BaseIntegrationTest` (`@SpringBootTest`,
    `@DirtiesContext(BEFORE_EACH_TEST_METHOD)`, so they are slow) and **must** use `@TestTemplate`, not `@Test`.
  - `@MultiStoreTest` / `MultiStoreTestExtension` runs each test once against PostgreSQL (Testcontainers
    `postgres:17.0`) and once against SQLite. It switches profiles (`postgresql`, `sqlite` in `src/test/resources`) by
    setting the **JVM-wide system property** `spring.profiles.active`.
  - The profile is therefore not part of Spring's context cache key. When one variant fails to load its context, the
    failure threshold also skips the other variant.
  - They need Docker and a writable `/app/db`; with FILE_SYSTEM storage they also write to `/app/data`.
- **HTTP stubs:** OkHttp `mockwebserver3`, not `com.sun.net.httpserver`.
- **Gap:** nothing exercises S3 end to end. Defects A1 and A2 would have been caught by a test against MinIO.

---

## 7. Change recipes

### Add or change an extraction type

Touch every layer in [§3.1](#31-four-parallel-model-layers):

1. **Write and read DTOs** in `api/job/dto/{write,read}/extraction_configuration/<type>/`, and register the subtype in
   `@JsonSubTypes` on `WriteExtractionConfigurationDto`.
2. **Properties:** add `<Type>ExtractionConfigurationProperties` to the **same package** as `ExtractionTypeProperties`.
   Add it to the `permits` list, add a field plus a map entry in `ExtractionConfigurationProperties`
   (`getExtractionTypesByName`), and add a `switch` case in `ExtractionDefinitionMapperService`. The compiler enforces
   the last one.
3. **Entity:** `model/job_definition/.../extraction_definition/<type>/`, with `@DiscriminatorValue`.
4. **Runtime config:** `execution/model/task/extraction_configuration/<type>/`.
5. **Mappers:**
   - `mapper/job/scraping_configuration/extraction_definition/<type>/` (DTO/properties ↔ entity), plus
     `@SubclassMapping`s in `ExtractionDefinitionMapper`.
   - `mapper/task/configuration/extraction_configuration/<type>/` (entity → runtime), plus a `@SubclassMapping` in
     `ExtractionConfigurationMapper`.
6. **`ScrapingService`:** add a `switch` case. The class isn't sealed, so a missing case falls to the `default` throw at
   runtime.
7. **Migrations:** add the subtype table in **both** `db/sqlite` and `db/postgresql`.
8. **Docs:** update `docs/configuration/jobs.md` and `docs/api_specs/job_api.yaml`.

### Add an entity package

Add it to the scan list in **both** `SqlitePersistenceConfig` and `PostgreSqlPersistenceConfig`, and write migrations
for both backends.

### Add a result storage backend

1. Subclass `ResultDataStoreProvider` and gate it with
   `@ConditionalOnProperty(name = "beluga.result-storage.type", havingValue = "<TYPE>")`.
2. Add the enum value to `DataStoreType`, a prefix branch to `NamespaceFactory`, and properties to
   `ResultStorageProperties`.
3. Extend `ResultStorageBackendSelectionTest`.

### Add a new event or listener

- **Async listener:** add `@Async` if it does I/O, and re-establish logging context with `logger.with(...)`, because the
  MDC is cleared.
- **Synchronous listener:** it runs on the publisher's thread and inside the publisher's failure path. In `Worker`, an
  exception from a synchronous listener turns the task into FAILED.
- Add the event to the catalogue in [§4.4](#44-event-catalogue).

### Change execution flow or in-memory state

Keep [the invariants](#architectural-invariants). Review E4–E7 first, because they involve the same state.
