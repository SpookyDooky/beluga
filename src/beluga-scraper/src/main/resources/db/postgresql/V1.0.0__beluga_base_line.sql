create table execution_definition
(
    id               bigint generated always as identity primary key,
    workers          bigint           not null,
    tasks_per_second double precision not null
);

create table scraping_definition
(
    id            bigint generated always as identity primary key,
    item_selector varchar not null
);

create table url_configuration
(
    id       bigint generated always as identity primary key,
    url_file varchar
);

create table urls
(
    id                   bigint generated always as identity primary key,
    url_configuration_id bigint  not null,
    url                  varchar not null,
    constraint fk_url_configuration_id
        foreign key (url_configuration_id)
            references url_configuration (id)
);

create table job_definition
(
    id                      bigint generated always as identity primary key,
    name                    varchar not null unique,
    url_configuration_id    bigint,
    scraping_definition_id  bigint  not null,
    execution_definition_id bigint  not null,
    constraint fk_url_configuration_id
        foreign key (url_configuration_id)
            references url_configuration (id),
    constraint fk_scraping_definition_id
        foreign key (scraping_definition_id)
            references scraping_definition (id),
    constraint fk_execution_definition_id
        foreign key (execution_definition_id)
            references execution_definition (id)
);

create table job_execution
(
    id                bigint generated always as identity primary key,
    job_definition_id bigint    not null,
    executed_at       timestamp not null,
    status            varchar   not null default 'PLANNED',
    constraint fk_job_definition_id
        foreign key (job_definition_id)
            references job_definition (id)
);

create table task_definition
(
    id                bigint generated always as identity primary key,
    job_definition_id bigint  not null,
    url               varchar not null,
    active            bool default true,
    constraint fk_job_definition_id
        foreign key (job_definition_id)
            references job_definition (id)
);

create table task_execution
(
    id                 bigint generated always as identity primary key,
    job_execution_id   bigint not null,
    task_definition_id bigint not null,
    executed_at        timestamp,
    status             varchar,
    constraint fk_job_execution_id
        foreign key (job_execution_id)
            references job_execution (id),
    constraint fk_task_definition_id
        foreign key (task_definition_id)
            references task_definition (id)
);

create table result_file
(
    id                bigint generated always as identity primary key,
    namespace         varchar not null,
    size_in_bytes     bigint  not null,
    compression_type  varchar not null,
    task_execution_id bigint  not null,
    key               varchar not null,
    constraint fk_task_execution_id
        foreign key (task_execution_id)
            references task_execution (id)
);

create table activity_log
(
    id        bigint generated always as identity primary key,
    timestamp timestamp not null,
    type      varchar   not null,
    context   jsonb     not null
);

create table extraction_definition
(
    id                     bigint generated always as identity primary key,
    scraping_definition_id bigint  not null,
    type                   varchar not null,
    constraint fk_scraping_definition_id
        foreign key (scraping_definition_id)
            references scraping_definition (id)
);

create table text_extraction_definition
(
    id       bigint primary key,
    selector varchar not null,
    field    varchar not null,
    constraint fk_extraction_definition_id
        foreign key (id)
            references extraction_definition (id)
);

create table html_extraction_definition
(
    id       bigint primary key,
    selector varchar not null,
    field    varchar not null,
    constraint fk_extraction_definition_id
        foreign key (id)
            references extraction_definition (id)
);

create table attribute_extraction_definition
(
    id        bigint primary key,
    selector  varchar not null,
    field     varchar not null,
    attribute varchar not null,
    constraint fk_extraction_definition_id
        foreign key (id)
            references extraction_definition (id)
);

create table image_extraction_definition
(
    id bigint primary key,
    constraint fk_extraction_definition_id
        foreign key (id)
            references extraction_definition (id)
);

create table description_list_extraction_definition
(
    id       bigint primary key,
    selector varchar not null,
    constraint fk_extraction_definition_id
        foreign key (id)
            references extraction_definition (id)
);

create table description_list_extraction_data_point_definition
(
    id                                        bigint generated always as identity primary key,
    dt_value                                  varchar not null,
    field                                     varchar not null,
    description_list_extraction_definition_id bigint  not null,
    constraint fk_description_list_extraction_definition_id
        foreign key (description_list_extraction_definition_id)
            references description_list_extraction_definition (id)
);