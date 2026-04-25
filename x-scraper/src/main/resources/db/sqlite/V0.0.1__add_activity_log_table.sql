create table activity_log (
	id integer primary key autoincrement not null,
	"timestamp" text not null,
	"type" varchar not null,
	context jsonb not null
);