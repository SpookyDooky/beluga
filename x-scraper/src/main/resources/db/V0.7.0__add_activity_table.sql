create table activity_log
(
    id
        bigint
        not null
        primary key default nextval('primary_sequence'),
    timestamp
        timestamp
        not null,
    type
        varchar
        not null,
    context
        jsonb
        not null
)