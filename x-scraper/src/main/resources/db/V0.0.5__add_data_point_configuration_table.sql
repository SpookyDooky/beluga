create table data_point_configuration(
    id bigint not null primary key default nextval('primary_sequence'),
    scraping_configuration_id bigint not null,
    selector varchar not null,
    property_name varchar not null,
    attribute varchar,
    type varchar not null,
    constraint fk_scraping_configuration_id
        foreign key (scraping_configuration_id)
        references scraping_configuration(id)
);