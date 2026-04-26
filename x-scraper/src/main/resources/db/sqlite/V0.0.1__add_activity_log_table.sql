create table activity_log (
	id INTEGER PRIMARY KEY,
	timestamp text not null,
	"type" varchar not null,
	context jsonb not null
);