-- Fixes the incorrectly configured foreign key references

create table image_extraction_definition_new
(
    id integer primary key,
    foreign key (id) references extraction_definition (id)
);

insert into image_extraction_definition_new (id)
select id
from image_extraction_definition;

drop table image_extraction_definition;
alter table image_extraction_definition_new rename to image_extraction_definition;

create table description_list_extraction_definition_new
(
    id       integer primary key,
    selector varchar not null,
    foreign key (id) references extraction_definition (id)
);

insert into description_list_extraction_definition_new (id, selector)
select id, selector
from description_list_extraction_definition;

create table description_list_extraction_data_point_definition_new
(
    id                                        integer primary key,
    dt_value                                  varchar not null,
    field                                     varchar not null,
    description_list_extraction_definition_id integer not null,
    foreign key (description_list_extraction_definition_id) references description_list_extraction_definition_new (id)
);

insert into description_list_extraction_data_point_definition_new (id, dt_value, field, description_list_extraction_definition_id)
select id, dt_value, field, description_list_extraction_definition_id
from description_list_extraction_data_point_definition;

drop table description_list_extraction_data_point_definition;
drop table description_list_extraction_definition;
alter table description_list_extraction_definition_new rename to description_list_extraction_definition;
alter table description_list_extraction_data_point_definition_new rename to description_list_extraction_data_point_definition;
