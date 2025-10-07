--liquibase formatted sql

--changeset your-name:2025-09-19--initial-schema-1
--comment Create users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    user_name VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(50),
    account_id BIGINT
);

CREATE UNIQUE INDEX users_user_name_unique ON users (user_name);

--changeset your-name:2025-09-19--initial-schema-2
--comment Create accounts table with foreign key
CREATE TABLE accounts (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    balance DECIMAL(19,2),
    user_id BIGINT
);

CREATE UNIQUE INDEX accounts_user_id_unique ON accounts (user_id);

ALTER TABLE accounts 
    ADD CONSTRAINT fk_accounts_user_id 
    FOREIGN KEY (user_id) 
    REFERENCES users (id);

--changeset your-name:2025-09-19--initial-schema-3
--comment Create payments table with foreign key
CREATE TABLE payments (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    buy VARCHAR(255),
    buy_id BIGINT,
    user_id BIGINT,
    price DECIMAL(19,2),
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL
);

ALTER TABLE payments 
    ADD CONSTRAINT fk_payments_user_id 
    FOREIGN KEY (user_id) 
    REFERENCES users (id);