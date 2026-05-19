create table job_definition(
    id bigint primary key not null default nextval('primary_sequence'),
    name varchar not null unique,
    url_configuration_id bigint not null,
    scraping_definition_id bigint not null,
    execution_definition_id bigint not null,
    constraint fk_url_configuration_id
        foreign key (url_configuration_id)
        references url_configuration(id),
    constraint fk_scraping_definition_id
        foreign key (scraping_definition_id)
        references scraping_definition(id),
    constraint fk_execution_definition_id
        foreign key (execution_definition_id)
        references execution_definition(id)
);