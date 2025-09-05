-- -----------------------
-- Пользователи с паролем "a"
-- -----------------------
INSERT INTO users (user_name, password, role)
VALUES
('a', '$2a$12$VB58SR8NBHK7v3GkKs251up87S1b7MUREhiKzadCXf5XNwRvMZ736', 'ROLE_USER'),
('b', '$2a$12$VB58SR8NBHK7v3GkKs251up87S1b7MUREhiKzadCXf5XNwRvMZ736', 'ROLE_USER'),
('admin', '$2a$12$VB58SR8NBHK7v3GkKs251up87S1b7MUREhiKzadCXf5XNwRvMZ736', 'ROLE_ADMIN');

-- -----------------------
-- Комнаты
-- -----------------------
INSERT INTO rooms (room_number, type, price_per_day)
VALUES
(101, 'STANDARD', 50.00),
(102, 'PREMIUM', 80.00),
(103, 'RESERVE', 150.00);

-- -----------------------
-- Заказы
-- -----------------------
INSERT INTO orders (begin_rent, end_rent, created_at, total_price, status, user_id, room_id)
VALUES
('2025-09-01', '2025-09-10', '2025-09-01 12:00', 100.00, 'NOT_PAID', 1, 1),
('2025-09-05', '2025-09-07',  '2025-09-01 12:00', 160.00, 'PAID', 2, 2);

-- -----------------------
-- ACL SID (пользователи и роли)
-- -----------------------
INSERT INTO acl_sid (principal, sid) VALUES
(true, 'a'),
(true, 'b'),
(true, 'admin'),
(false, 'ROLE_ADMIN'),
(false, 'ROLE_USER');

-- -----------------------
-- ACL CLASS для заказов
-- -----------------------
INSERT INTO acl_class (class) VALUES
('ru.otus.hw.models.Room');

-- -----------------------
-- ACL OBJECTS для заказов
-- -----------------------
-- допустим id 1 и 2 заказов
INSERT INTO acl_object_identity (object_id_class, object_id_identity, owner_sid, entries_inheriting)
VALUES
(1, 1, 1, TRUE),
(1, 2, 2, TRUE),
(1, 3, 3, FALSE);

-- -----------------------
-- ACL ENTRIES
-- -----------------------
INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
VALUES
(1, 0, 5, 1, TRUE, FALSE, FALSE),
(2, 0, 5, 1, TRUE, FALSE, FALSE);

-- Права на комнату типа RESERVE только для ROLE_ADMIN
INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
VALUES
(1, 1, 4, 1, TRUE, FALSE, FALSE),
(2, 1, 4, 1, TRUE, FALSE, FALSE),
(3, 0, 4, 1, TRUE, FALSE, FALSE);