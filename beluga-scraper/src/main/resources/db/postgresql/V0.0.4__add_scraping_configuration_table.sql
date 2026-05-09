create table scraping_configuration(
    id bigint not null primary key default nextval('primary_sequence'),
    element_selector varchar not null
);