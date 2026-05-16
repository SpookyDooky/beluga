create table result_file(
    id bigint not null primary key default nextval('primary_sequence'),
    namespace varchar not null,
    size_in_bytes bigint not null,
    compression_type varchar not null,
    task_execution_id bigint not null,
    constraint fk_task_execution_id
        foreign key (task_execution_id)
        references task_execution(id)
)