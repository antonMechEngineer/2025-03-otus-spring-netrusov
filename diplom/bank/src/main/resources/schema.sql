CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    user_name VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(50),
    account_id BIGINT
);

-- Создание таблицы accounts
CREATE TABLE IF NOT EXISTS accounts (
    id BIGSERIAL PRIMARY KEY,
    balance DECIMAL(19, 2),
    user_id BIGINT UNIQUE REFERENCES users(id)
);

-- Создание таблицы payments
CREATE TABLE IF NOT EXISTS payments (
    id BIGSERIAL PRIMARY KEY,
    buy VARCHAR(255),
    buy_id BIGINT,
    user_id BIGINT REFERENCES users(id),
    price DECIMAL(19, 2),
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);