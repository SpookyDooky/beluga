create table scraping_configuration(
    id bigint not null primary key default nextval('primary_sequence'),
    item_selector varchar not null
);