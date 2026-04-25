CREATE TABLE job_definition (
	id integer primary key autoincrement not null,
	"name" varchar NOT NULL,
	scraping_definition_id integer NOT NULL,
	storage_definition_id integer NOT NULL,
	execution_definition_id integer NOT NULL,
	CONSTRAINT job_definition_name_key UNIQUE (name),
	FOREIGN KEY (execution_definition_id) REFERENCES execution_definition(id),
	FOREIGN KEY (scraping_definition_id) REFERENCES scraping_definition(id),
	FOREIGN KEY (storage_definition_id) REFERENCES storage_definition(id)
);