INSERT INTO users (user_name, password, role, account_id)
VALUES
('a', '$2a$12$VB58SR8NBHK7v3GkKs251up87S1b7MUREhiKzadCXf5XNwRvMZ736', 'ROLE_USER', 1),
('b', '$2a$12$VB58SR8NBHK7v3GkKs251up87S1b7MUREhiKzadCXf5XNwRvMZ736', 'ROLE_USER', 2),
('admin', '$2a$12$VB58SR8NBHK7v3GkKs251up87S1b7MUREhiKzadCXf5XNwRvMZ736', 'ROLE_ADMIN', 3);


INSERT INTO accounts (balance, user_id)
VALUES
(1000.00, 1),
(500.00, 2),
(500.00, 3);

INSERT INTO payments (buy, buy_id, user_id, price, status)
VALUES
('Hotel', 1, 1, 50.00, 'NOT_PAID'),
('Hotel', 2, 2, 20.00, 'PAID');