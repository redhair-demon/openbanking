--liquibase formatted sql

--changeset negorov:users
--comment добавление таблицы users
CREATE TABLE users
(
    id                       BIGSERIAL PRIMARY KEY,
    name                     TEXT      NOT NULL,
    password_hash            UUID      NOT NULL,
    phone                    TEXT      NOT NULL,
    created_at               TIMESTAMP NOT NULL DEFAULT now(),
    updated_at               TIMESTAMP NOT NULL DEFAULT now()
);

--changeset negorov:add-users
--comment добавление data users
INSERT INTO users VALUES (1, 'Adam', '098f6bcd-4621-3373-8ade-4e832627b4f6'::uuid, '89121234567'),
                         (2, 'Bob', 'ad023482-9205-3903-b196-ba818f7a872b'::uuid, '89127654321')