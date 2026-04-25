create table task_execution(
    id bigint not null primary key default nextval('primary_sequence'),
    job_execution_id bigint not null,
    task_definition_id bigint not null,
    executed_at timestamp,
    status varchar,
    result_folder varchar,
    constraint fk_job_execution_id
        foreign key (job_execution_id)
        references job_execution(id),
    constraint fk_task_definition_id
        foreign key (task_definition_id)
        references task_definition(id)
)