create table job_definition(
    id bigint primary key not null default nextval('primary_sequence'),
    name varchar not null unique,
    url_configuration_id bigint not null,
    scraping_configuration_id bigint not null,
    storage_configuration_id bigint not null,
    execution_configuration_id bigint not null,
    constraint fk_url_configuration_id
        foreign key (url_configuration_id)
        references url_configuration(id),
    constraint fk_scraping_configuration_id
        foreign key (scraping_configuration_id)
        references scraping_configuration(id),
    constraint fk_storage_configuration_id
        foreign key (storage_configuration_id)
        references storage_configuration(id),
    constraint fk_execution_configuration_id
        foreign key (execution_configuration_id)
        references execution_configuration(id)
);