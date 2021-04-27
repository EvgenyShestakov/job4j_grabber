create table post(
id serial primary key,
name varchar(255),
text varchar(255),
link varchar(255) unique,
created timestamp
);