create table urls(
    id bigint not null primary key default nextval('primary_sequence'),
    url_configuration_id bigint not null,
    url varchar not null,
    constraint fk_url_configuration_id
        foreign key (url_configuration_id)
        references url_configuration(id)
)