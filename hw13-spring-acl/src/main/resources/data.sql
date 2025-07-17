-- Вставка данных в таблицу authors
INSERT INTO authors (full_name)
VALUES ('Author_1'), ('Author_2'), ('Author_3');

-- Вставка данных в таблицу genres
INSERT INTO genres (name)
VALUES ('Genre_1'), ('Genre_2'), ('Genre_3');

-- Вставка данных в таблицу books
INSERT INTO books (title, author_id, genre_id)
VALUES ('BookTitle_1', 1, 1), ('BookTitle_2', 2, 2), ('BookTitle_3', 3, 3);

-- Вставка данных в таблицу comments
INSERT INTO comments (payload_comment, book_id)
VALUES ('Comment_1_to_book_1', 1), ('Comment_2_to_book_1', 1), ('Comment_2_to_book_2', 2);

-- Вставка данных в таблицу users
INSERT INTO users (user_name, password, role)
VALUES
    ('abc', '$2a$12$i9Dekxpdi0fS1IyILNiEsOVewcLRXPajF6bX4JhuD2BasY5l00FLe', 'USER'),
    ('def', '$2a$12$i9Dekxpdi0fS1IyILNiEsOVewcLRXPajF6bX4JhuD2BasY5l00FLe', 'ADMIN');

-- Вставка данных в таблицу acl_sid
INSERT INTO acl_sid (id, principal, sid)
VALUES
    (1, 1, 'abc'), -- Пользователь 'abc'
    (2, 0, 'ROLE_USER'), -- Роль USER
    (3, 0, 'ROLE_ADMIN'), -- Роль ADMIN
    (4, 1, 'def'); -- Пользователь 'def'

-- Вставка данных в таблицу acl_class
INSERT INTO acl_class (id, class)
VALUES
    (1, 'ru.otus.hw.models.Author'),
    (2, 'ru.otus.hw.models.Genre'),
    (3, 'ru.otus.hw.models.Book'),
    (4, 'ru.otus.hw.models.Comment');

-- Порядок должен соответствовать id, которые потом используются
INSERT INTO acl_object_identity (object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
VALUES
    (1, 1, NULL, 1, FALSE), -- Автор 1
    (1, 2, NULL, 1, FALSE), -- Автор 2
    (1, 3, NULL, 1, FALSE), -- Автор 3
    (2, 1, NULL, 1, FALSE), -- Жанр 1
    (2, 2, NULL, 1, FALSE), -- Жанр 2
    (2, 3, NULL, 1, FALSE), -- Жанр 3
    (3, 1, NULL, 1, FALSE), -- Книга 1
    (3, 2, NULL, 1, FALSE), -- Книга 2
    (3, 3, NULL, 1, FALSE), -- Книга 3
    (4, 1, NULL, 1, FALSE), -- Комментарий 1
    (4, 2, NULL, 1, FALSE), -- Комментарий 2
    (4, 3, NULL, 1, FALSE); -- Комментарий 3

-- Вставка данных в таблицу acl_entry
-- Права для роли USER
INSERT INTO acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
VALUES
    -- Права на чтение для всех объектов
    (1, 1, 1, 2, 1, 1, 1, 1), -- Автор 1
    (2, 2, 1, 2, 1, 1, 1, 1), -- Автор 2
    (3, 3, 1, 2, 1, 1, 1, 1), -- Автор 3
    (4, 4, 1, 2, 1, 1, 1, 1), -- Жанр 1
    (5, 5, 1, 2, 1, 1, 1, 1), -- Жанр 2
    (6, 6, 1, 2, 1, 1, 1, 1), -- Жанр 3
    (7, 7, 1, 2, 1, 1, 1, 1), -- Книга 1
    (8, 8, 1, 2, 1, 1, 1, 1), -- Книга 2
    (9, 9, 1, 2, 1, 1, 1, 1), -- Книга 3
    (10, 10, 1, 2, 1, 1, 1, 1), -- Комментарий 1
    (11, 11, 1, 2, 1, 1, 1, 1), -- Комментарий 2
    (12, 12, 1, 2, 1, 1, 1, 1); -- Комментарий 3




