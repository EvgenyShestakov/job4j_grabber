create table post(
id serial primary key,
name text,
text text unique,
link text,
created timestamp
);