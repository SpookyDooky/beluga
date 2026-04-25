CREATE TABLE data_point_definition (
	id integer primary key autoincrement not null,
	scraping_definition_id integer NOT NULL,
	selector varchar NOT NULL,
	property_name varchar NOT NULL,
	"attribute" varchar NULL,
	"type" varchar NOT NULL,
	FOREIGN KEY (scraping_definition_id) REFERENCES scraping_definition(id)
);