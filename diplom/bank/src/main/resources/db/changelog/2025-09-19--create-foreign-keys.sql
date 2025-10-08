--liquibase formatted sql

--changeset your-name:2025-09-19--create-foreign-keys-1
--comment Add foreign keys for acl_entry table
ALTER TABLE acl_entry 
    ADD CONSTRAINT fk_acl_entry_object_identity 
    FOREIGN KEY (acl_object_identity) 
    REFERENCES acl_object_identity (id);

ALTER TABLE acl_entry 
    ADD CONSTRAINT fk_acl_entry_sid 
    FOREIGN KEY (sid) 
    REFERENCES acl_sid (id);

--changeset your-name:2025-09-19--create-foreign-keys-2
--comment Add foreign keys for acl_object_identity table
ALTER TABLE acl_object_identity 
    ADD CONSTRAINT fk_acl_oi_parent 
    FOREIGN KEY (parent_object) 
    REFERENCES acl_object_identity (id);

ALTER TABLE acl_object_identity 
    ADD CONSTRAINT fk_acl_oi_class 
    FOREIGN KEY (object_id_class) 
    REFERENCES acl_class (id);

ALTER TABLE acl_object_identity 
    ADD CONSTRAINT fk_acl_oi_owner_sid 
    FOREIGN KEY (owner_sid) 
    REFERENCES acl_sid (id);