create table task_execution_file(
    id bigint not null primary key default nextval('primary_sequence'),
    task_execution_id bigint not null,
    file_id bigint not null,
    constraint fk_task_execution_id
        foreign key (task_execution_id)
        references task_execution(id),
    constraint fk_file_id
        foreign key (file_id)
        references file(id)
)