create table scraping_definition(
    id bigint not null primary key default nextval('primary_sequence'),
    item_selector varchar not null
);