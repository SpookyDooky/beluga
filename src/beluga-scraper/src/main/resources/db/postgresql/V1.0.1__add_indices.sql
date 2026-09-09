create index if not exists job_execution_id_index on task_execution(job_execution_id);
create index if not exists task_execution_id_index on result_file(task_execution_id);
create index if not exists key_index on result_file(key);