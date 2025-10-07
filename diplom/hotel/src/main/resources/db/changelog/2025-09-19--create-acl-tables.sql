--liquibase formatted sql

--changeset ANN:2025-09-19--create-acl-tables-1
--comment Create acl_sid table
CREATE TABLE acl_sid (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    principal BOOLEAN NOT NULL,
    sid VARCHAR(100) NOT NULL
);

CREATE UNIQUE INDEX unique_uk_1 ON acl_sid (sid, principal);

--changeset ANN:2025-09-19--create-acl-tables-2
--comment Create acl_class table
CREATE TABLE acl_class (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    class VARCHAR(255) NOT NULL
);

CREATE UNIQUE INDEX unique_uk_2 ON acl_class (class);

--changeset ANN:2025-09-19--create-acl-tables-3
--comment Create acl_object_identity table
CREATE TABLE acl_object_identity (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    object_id_class BIGINT NOT NULL,
    object_id_identity VARCHAR(255) NOT NULL,
    parent_object BIGINT,
    owner_sid BIGINT,
    entries_inheriting BOOLEAN NOT NULL
);

CREATE UNIQUE INDEX unique_uk_3 ON acl_object_identity (object_id_class, object_id_identity);

--changeset ANN:2025-09-19--create-acl-tables-4
--comment Create acl_entry table
CREATE TABLE acl_entry (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    acl_object_identity BIGINT NOT NULL,
    ace_order INT NOT NULL,
    sid BIGINT NOT NULL,
    mask INT NOT NULL,
    granting BOOLEAN NOT NULL,
    audit_success BOOLEAN NOT NULL,
    audit_failure BOOLEAN NOT NULL
);

CREATE UNIQUE INDEX unique_uk_4 ON acl_entry (acl_object_identity, ace_order);