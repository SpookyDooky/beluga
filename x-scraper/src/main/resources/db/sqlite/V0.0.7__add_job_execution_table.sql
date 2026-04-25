CREATE TABLE job_execution (
	id INTEGER PRIMARY KEY,
	job_definition_id integer NOT NULL,
	executed_at text NOT NULL,
	status varchar DEFAULT 'PLANNED' NOT NULL,
	FOREIGN KEY (job_definition_id) REFERENCES job_definition(id)
);