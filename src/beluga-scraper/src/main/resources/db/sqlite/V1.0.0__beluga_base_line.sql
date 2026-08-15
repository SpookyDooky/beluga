create table activity_log
(
    id        INTEGER PRIMARY KEY,
    timestamp text    not null,
    "type"    varchar not null,
    context   jsonb   not null
);

create table scraping_definition
(
    id            INTEGER PRIMARY KEY,
    item_selector varchar not null
);

CREATE TABLE extraction_definition
(
    id   INTEGER PRIMARY KEY,
    type varchar not null
);

create table text_extraction_definition
(
    id    integer primary key,
    field varchar not null,
    foreign key (id) references extraction_definition (id)
);

create table html_extraction_definition
(
    id    integer primary key,
    field varchar not null,
    foreign key (id) references extraction_definition (id)
);

create table attribute_extraction_definition
(
    id        integer primary key,
    field     varchar not null,
    attribute varchar not null,
    foreign key (id) references extraction_definition (id)
);

create table image_extraction_definition
(
    id integer primary key,
    foreign key (id) references execution_definition (id)
);

create table description_list_extraction_definition
(
    id integer primary key,
    foreign key (id) references execution_definition (id)
);

create table description_list_extraction_data_point_definition
(
    id integer primary key,
    dt_value varchar not null,
    field varchar not null,
    description_list_extraction_definition_id integer not null,
    foreign key (description_list_extraction_definition_id) references  description_list_extraction_definition(id)
);

CREATE TABLE data_point_definition
(
    id                     INTEGER PRIMARY KEY,
    scraping_definition_id integer NOT NULL,
    selector               varchar NOT NULL,
    extraction_definition_id integer not null,
    FOREIGN KEY (scraping_definition_id) REFERENCES scraping_definition (id),
    foreign key (extraction_definition_id) references extraction_definition(id)
);

CREATE TABLE execution_definition
(
    id               INTEGER PRIMARY KEY,
    workers          integer NOT NULL,
    tasks_per_second float   NOT NULL
);

CREATE TABLE url_configuration
(
    id       INTEGER PRIMARY KEY,
    url_file varchar NULL
);

CREATE TABLE job_definition
(
    id                      INTEGER PRIMARY KEY,
    "name"                  varchar NOT NULL,
    scraping_definition_id  integer NOT NULL,
    execution_definition_id integer NOT NULL,
    url_configuration_id    integer,
    CONSTRAINT job_definition_name_key UNIQUE (name),
    FOREIGN KEY (execution_definition_id) REFERENCES execution_definition (id),
    FOREIGN KEY (scraping_definition_id) REFERENCES scraping_definition (id),
    FOREIGN KEY (url_configuration_id) REFERENCES url_configuration (id)
);

CREATE TABLE job_execution
(
    id                INTEGER PRIMARY KEY,
    job_definition_id integer                   NOT NULL,
    executed_at       text                      NOT NULL,
    status            varchar DEFAULT 'PLANNED' NOT NULL,
    FOREIGN KEY (job_definition_id) REFERENCES job_definition (id)
);

CREATE TABLE task_definition
(
    id                integer primary key autoincrement,
    job_definition_id integer NOT NULL,
    url               varchar NOT NULL,
    active            integer DEFAULT 1 NULL,
    FOREIGN KEY (job_definition_id) REFERENCES job_definition (id)
);

CREATE TABLE task_execution
(
    id                 INTEGER PRIMARY KEY,
    job_execution_id   integer NOT NULL,
    task_definition_id integer NOT NULL,
    executed_at        text NULL,
    status             varchar NULL,
    FOREIGN KEY (job_execution_id) REFERENCES job_execution (id),
    FOREIGN KEY (task_definition_id) REFERENCES task_definition (id)
);

CREATE TABLE result_file
(
    id                INTEGER PRIMARY KEY,
    "namespace"       varchar NOT NULL,
    size_in_bytes     integer NOT NULL,
    compression_type  varchar NOT NULL,
    task_execution_id integer NOT NULL,
    key               varchar NULL,
    FOREIGN KEY (task_execution_id) REFERENCES task_execution (id)
);