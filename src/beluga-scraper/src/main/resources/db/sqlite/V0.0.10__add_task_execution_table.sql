CREATE TABLE task_execution (
	id INTEGER PRIMARY KEY,
	job_execution_id integer NOT NULL,
	task_definition_id integer NOT NULL,
	executed_at text NULL,
	status varchar NULL,
	result_folder varchar NULL,
	FOREIGN KEY (job_execution_id) REFERENCES job_execution(id),
	FOREIGN KEY (task_definition_id) REFERENCES task_definition(id)
);