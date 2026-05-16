create table storage_configuration(
    id bigint not null primary key default nextval('primary_sequence'),
    format varchar
)