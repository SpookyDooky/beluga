CREATE TABLE task_definition (
	id integer primary key autoincrement not null,
	job_definition_id integer NOT NULL,
	url varchar NOT NULL,
	active integer DEFAULT 1 NULL,
	FOREIGN KEY (job_definition_id) REFERENCES job_definition(id)
);