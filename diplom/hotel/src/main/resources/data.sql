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
(103, 'LUX', 150.00);

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
('ru.otus.hw.models.Order');

