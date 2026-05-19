create table execution_definition(
    id bigint not null primary key default nextval('primary_sequence'),
    workers bigint not null,
    tasks_per_second bigint not null
);