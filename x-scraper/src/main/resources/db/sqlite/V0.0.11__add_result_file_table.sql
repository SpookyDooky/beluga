CREATE TABLE result_file (
	id integer primary key autoincrement not null,
	"path" varchar NOT NULL,
	size_in_bytes integer NOT NULL,
	compression_type varchar NOT NULL,
	task_execution_id integer NOT NULL,
	file_name varchar NULL,
    FOREIGN KEY (task_execution_id) REFERENCES task_execution(id)
);