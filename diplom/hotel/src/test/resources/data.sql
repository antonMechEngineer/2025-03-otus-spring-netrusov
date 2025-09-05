-- -----------------------
INSERT INTO users (user_name, password)
VALUES
('user1', '$2a$10$Dow1HZLZwR4GfOZlQjH6heCz/1nLMCwTQr/Y0fP1GB2r2Kq6pqDna'),
('user2', '$2a$10$Dow1HZLZwR4GfOZlQjH6heCz/1nLMCwTQr/Y0fP1GB2r2Kq6pqDna'),
('admin', '$2a$10$Dow1HZLZwR4GfOZlQjH6heCz/1nLMCwTQr/Y0fP1GB2r2Kq6pqDna');

-- -----------------------
-- Комнаты
-- -----------------------
INSERT INTO rooms (room_number, type, price_per_day)
VALUES
(101, 'STANDARD', 50.00),
(102, 'PREMIUM', 80.00);

-- -----------------------
-- Заказы
-- -----------------------
INSERT INTO orders (begin_rent, end_rent, total_price, is_paid, user_id, room_id)
VALUES
('2025-09-01 14:00', '2025-09-03 12:00', 100.00, 'PENDING', 1, 101),
('2025-09-05 15:00', '2025-09-07 11:00', 160.00, 'SUCCESSFUL', 2, 102);

-- -----------------------
-- ACL SID (пользователи и роли)
-- -----------------------
INSERT INTO acl_sid (principal, sid) VALUES
(true, 'user1'),
(true, 'user2'),
(true, 'admin'),
(false, 'ROLE_ADMIN');

-- -----------------------
-- ACL CLASS для заказов
-- -----------------------
INSERT INTO acl_class (class) VALUES
('ru.otus.hw.models.Order');

-- -----------------------
-- ACL OBJECTS для заказов
-- -----------------------
-- допустим id 1 и 2 заказов
INSERT INTO acl_object_identity (object_id_class, object_id_identity, owner_sid, entries_inheriting)
VALUES
(1, 1, 1, TRUE),
(1, 2, 2, TRUE);

-- -----------------------
-- ACL ENTRIES
-- -----------------------
-- даём полные права владельцам
INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
VALUES
(1, 0, 1, 15, TRUE, FALSE, FALSE),
(2, 0, 2, 15, TRUE, FALSE, FALSE);
