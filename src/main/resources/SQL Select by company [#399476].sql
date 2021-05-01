CREATE TABLE company
(
    id integer NOT NULL,
    name character varying,
    CONSTRAINT company_pkey PRIMARY KEY (id)
);
CREATE TABLE person
(
    id integer NOT NULL,
    name character varying,
    company_id integer,
    CONSTRAINT person_pkey PRIMARY KEY (id)
);
INSERT INTO company(id, name) VALUES (1, 'Samsung');
INSERT INTO company(id, name) VALUES (2, 'LG');
INSERT INTO company(id, name) VALUES (3, 'Sony');
INSERT INTO company(id, name) VALUES (4, 'Microsoft');
INSERT INTO company(id, name) VALUES (5, 'Apple');
INSERT INTO person(id, name, company_id) VALUES (1, 'Tony', 3);
INSERT INTO person(id, name, company_id) VALUES (2, 'Jeff', 2);
INSERT INTO person(id, name, company_id) VALUES (3, 'John', 1);
INSERT INTO person(id, name, company_id) VALUES (4, 'Steve', 5);
INSERT INTO person(id, name, company_id) VALUES (5, 'Bob', 4);
INSERT INTO person(id, name, company_id) VALUES (6, 'Bill', 2);
INSERT INTO person(id, name, company_id) VALUES (7, 'Donald', 5);
INSERT INTO person(id, name) VALUES (8, 'Marry');
INSERT INTO person(id, name, company_id) VALUES (9, 'Pit', 5);
INSERT INTO person(id, name) VALUES (10, 'Sue');

SELECT p.name AS Имя, c.name AS Компания FROM person p LEFT JOIN company c on p.company_id = c.id
WHERE p.company_id != 5;
SELECT c.name AS Компания,  COUNT(p.company_id) AS Количество_человек FROM person p RIGHT JOIN company c
ON p.company_id = c.id GROUP BY c.name ORDER BY count(*) DESC LIMIT 1;