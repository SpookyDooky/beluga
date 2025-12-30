create table file(
    id bigint not null primary key default nextval('primary_sequence'),
    path varchar not null,
    size_in_bytes bigint not null,
    compression_type varchar not null
)