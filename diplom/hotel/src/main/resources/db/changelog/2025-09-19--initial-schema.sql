--liquibase formatted sql

--changeset ANN:2025-09-19--initial-schema-1
--comment Create users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    user_name VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(50)
);

CREATE UNIQUE INDEX users_user_name_unique ON users (user_name);

--changeset ANN:2025-09-19--initial-schema-2
--comment Create rooms table
CREATE TABLE rooms (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    room_number INT NOT NULL,
    type VARCHAR(20) NOT NULL,
    price_per_day DECIMAL(19,2) NOT NULL
);

--changeset ANN:2025-09-19--initial-schema-3
--comment Create orders table
CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    begin_rent TIMESTAMP NOT NULL,
    end_rent TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL,
    total_price DECIMAL(19,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    user_id BIGINT,
    room_id BIGINT
);