CREATE DATABASE supermarket_ims;
USE supermarket_ims;

CREATE TABLE items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(100),
    price DECIMAL(10,2) NOT NULL,
    stock_quantity INT NOT NULL
);

CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(255),
    total_price DECIMAL(10,2),
    status ENUM('Processing', 'Completed', 'Cancelled') DEFAULT 'Processing'
);

CREATE TABLE order_items (
    order_id BIGINT,
    item_id BIGINT,
    quantity INT NOT NULL,
    PRIMARY KEY (order_id, item_id),
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE
);

CREATE TABLE payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT UNIQUE,
    payment_status ENUM('Pending', 'Success', 'Failed') DEFAULT 'Pending',
    payment_method ENUM('Cash', 'Card', 'Online'),
    transaction_id VARCHAR(255),
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);

CREATE TABLE stock_orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    item_id BIGINT,
    quantity INT NOT NULL,
    status ENUM('Pending', 'Approved', 'Rejected') DEFAULT 'Pending',
    FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE
);

CREATE TABLE suppliers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    contact_email VARCHAR(255) UNIQUE,
    phone_number VARCHAR(20)
);

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'STAFF', 'CUSTOMER') NOT NULL
);

CREATE TABLE cart (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE
);

CREATE TABLE refunds (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT UNIQUE,
    reason TEXT,
    status ENUM('Pending', 'Approved', 'Rejected') DEFAULT 'Pending',
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);

CREATE TABLE supplier_orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    supplier_id BIGINT,
    item_name VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    received BOOLEAN DEFAULT FALSE,
    order_date DATE,
    FOREIGN KEY (supplier_id) REFERENCES suppliers(id) ON DELETE SET NULL
);



INSERT INTO users (username, password, role) VALUES 
('admin123', 'xyz', 'ADMIN'),
('staff001', 'abcd', 'STAFF'),
('customer01', '1234', 'CUSTOMER');

INSERT INTO items (id, name, category, price, stock_quantity) VALUES
(1, 'Apple', 'Fruits', 2.5, 100),
(2, 'Banana', 'Fruits', 1.2, 150),
(3, 'Milk', 'Dairy', 3.0, 80),
(4, 'Bread', 'Bakery', 2.0, 60),
(5, 'Eggs', 'Dairy', 4.5, 90),
(6, 'Chicken Breast', 'Meat', 6.0, 50),
(7, 'Rice', 'Grains', 10.0, 40),
(8, 'Sugar', 'Essentials', 3.2, 70),
(9, 'Salt', 'Essentials', 1.0, 100),
(10, 'Soap', 'Personal Care', 5.0, 60);

