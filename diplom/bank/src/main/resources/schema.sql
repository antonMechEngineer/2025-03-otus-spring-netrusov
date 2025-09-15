CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    user_name VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(50),
    account_id BIGINT
);

-- Создание таблицы accounts
CREATE TABLE accounts (
    id BIGSERIAL PRIMARY KEY,
    balance DECIMAL(19, 2),
    user_id BIGINT UNIQUE REFERENCES users(id)
);

-- Создание таблицы payments
CREATE TABLE payments (
    id BIGSERIAL PRIMARY KEY,
    buy VARCHAR(255),
    buy_id BIGINT,
    user_id BIGINT REFERENCES users(id),
    price DECIMAL(19, 2),
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Создание таблицы acl_sid
CREATE TABLE acl_sid (
    id BIGSERIAL PRIMARY KEY,
    principal BOOLEAN NOT NULL,
    sid VARCHAR(100) NOT NULL,
    CONSTRAINT unique_uk_1 UNIQUE (sid, principal)
);

-- Создание таблицы acl_class
CREATE TABLE acl_class (
    id BIGSERIAL PRIMARY KEY,
    class VARCHAR(255) NOT NULL,
    CONSTRAINT unique_uk_2 UNIQUE (class)
);

-- Создание таблицы acl_object_identity
CREATE TABLE acl_object_identity (
    id BIGSERIAL PRIMARY KEY,
    object_id_class BIGINT NOT NULL,
    object_id_identity VARCHAR(255) NOT NULL,
    parent_object BIGINT DEFAULT NULL,
    owner_sid BIGINT DEFAULT NULL,
    entries_inheriting BOOLEAN NOT NULL,
    CONSTRAINT unique_uk_3 UNIQUE (object_id_class, object_id_identity)
);

-- Создание таблицы acl_entry
CREATE TABLE acl_entry (
    id BIGSERIAL PRIMARY KEY,
    acl_object_identity BIGINT NOT NULL,
    ace_order INT NOT NULL,
    sid BIGINT NOT NULL,
    mask INT NOT NULL,
    granting BOOLEAN NOT NULL,
    audit_success BOOLEAN NOT NULL,
    audit_failure BOOLEAN NOT NULL,
    CONSTRAINT unique_uk_4 UNIQUE (acl_object_identity, ace_order)
);

-- Добавление внешних ключей
ALTER TABLE acl_entry
ADD FOREIGN KEY (acl_object_identity) REFERENCES acl_object_identity(id);

ALTER TABLE acl_entry
ADD FOREIGN KEY (sid) REFERENCES acl_sid(id);

ALTER TABLE acl_object_identity
ADD FOREIGN KEY (parent_object) REFERENCES acl_object_identity(id);

ALTER TABLE acl_object_identity
ADD FOREIGN KEY (object_id_class) REFERENCES acl_class(id);

ALTER TABLE acl_object_identity
ADD FOREIGN KEY (owner_sid) REFERENCES acl_sid(id);