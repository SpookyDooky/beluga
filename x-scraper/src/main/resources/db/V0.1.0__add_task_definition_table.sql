create table(
    id bigint not null primary key default nextval('primary_sequence'),
    job_definition_id bigint not null,
    url varchar not null,
    constraint fk_job_definition_id
        foreign key (job_definition_id)
        references job_definition(id)
)