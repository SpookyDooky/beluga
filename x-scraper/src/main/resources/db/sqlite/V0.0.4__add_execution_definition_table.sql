CREATE TABLE execution_definition (
	id integer primary key autoincrement not null,
	workers integer NOT NULL,
	tasks_per_second float NOT NULL
);