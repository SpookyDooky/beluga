create table job_execution(
    id bigint not null primary key default nextval('primary_sequence'),
    job_definition_id bigint not null,
    executed_at timestamp not null,
    constraint fk_job_definition_id
        foreign key (job_definition_id)
        references job_definition(id)
)