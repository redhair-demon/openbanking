--liquibase formatted sql

--changeset negorov:charts
--comment добавление таблицы charts
CREATE TABLE charts
(
    id                       BIGSERIAL PRIMARY KEY,
    name                     TEXT      NOT NULL,
    date_from                TIMESTAMP NOT NULL,
    date_to                  TIMESTAMP NOT NULL,
    picked                   UUID[]    NOT NULL,
    created_at               TIMESTAMP NOT NULL DEFAULT now(),
    updated_at               TIMESTAMP NOT NULL DEFAULT now()
);
