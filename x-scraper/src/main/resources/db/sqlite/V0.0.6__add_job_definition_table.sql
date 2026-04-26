CREATE TABLE url_configuration (
	id INTEGER PRIMARY KEY,
	url_file varchar NULL
);

CREATE TABLE job_definition (
	id INTEGER PRIMARY KEY,
	"name" varchar NOT NULL,
	scraping_definition_id integer NOT NULL,
	storage_definition_id integer NOT NULL,
	execution_definition_id integer NOT NULL,
	url_configuration_id integer,
	CONSTRAINT job_definition_name_key UNIQUE (name),
	FOREIGN KEY (execution_definition_id) REFERENCES execution_definition(id),
	FOREIGN KEY (scraping_definition_id) REFERENCES scraping_definition(id),
	FOREIGN KEY (storage_definition_id) REFERENCES storage_definition(id),
	FOREIGN KEY (url_configuration_id) REFERENCES url_configuration(id)
);