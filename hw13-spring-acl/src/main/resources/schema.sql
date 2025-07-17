-- Создание таблицы authors
CREATE TABLE authors (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(255)
);

-- Создание таблицы genres
CREATE TABLE genres (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255)
);

-- Создание таблицы books
CREATE TABLE books (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255),
    author_id BIGINT REFERENCES authors (id) ON DELETE CASCADE,
    genre_id BIGINT REFERENCES genres(id) ON DELETE CASCADE
);

-- Создание таблицы comments
CREATE TABLE comments (
    id BIGSERIAL PRIMARY KEY,
    payload_comment VARCHAR(1000),
    book_id BIGINT REFERENCES books (id) ON DELETE CASCADE
);

-- Создание таблицы users
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    user_name VARCHAR(1000),
    password VARCHAR(1000),
    role VARCHAR(50)
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