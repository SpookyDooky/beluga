CREATE TABLE execution_definition (
	id INTEGER PRIMARY KEY,
	workers integer NOT NULL,
	tasks_per_second float NOT NULL
);