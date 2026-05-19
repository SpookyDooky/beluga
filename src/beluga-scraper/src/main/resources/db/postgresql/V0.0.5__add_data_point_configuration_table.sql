create table data_point_definition(
    id bigint not null primary key default nextval('primary_sequence'),
    scraping_definition_id bigint not null,
    selector varchar not null,
    field varchar not null,
    attribute varchar,
    type varchar not null,
    constraint fk_scraping_definition_id
        foreign key (scraping_definition_id)
        references scraping_definition(id)
);