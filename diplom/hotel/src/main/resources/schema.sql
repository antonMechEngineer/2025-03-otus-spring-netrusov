-- -----------------------
-- Таблица пользователей
-- -----------------------
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    user_name VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(50)
);

-- -----------------------
-- Таблица комнат
-- -----------------------
CREATE TABLE rooms (
    id BIGSERIAL PRIMARY KEY,
    room_number INT NOT NULL,
    type VARCHAR(20) NOT NULL,
    price_per_day NUMERIC(10,2) NOT NULL
);

-- -----------------------
-- Таблица заказов
-- -----------------------
CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    begin_rent TIMESTAMP NOT NULL,
    end_rent TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL,
    total_price NUMERIC(10,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    room_id BIGINT NOT NULL REFERENCES rooms(id)
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
    object_id_identity BIGINT NOT NULL,
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