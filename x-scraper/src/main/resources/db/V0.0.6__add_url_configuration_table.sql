create table url_configuration(
    id bigint not null primary key default nextval('primary_sequence'),
    url_file varchar
)