CREATE DATABASE IF NOT EXISTS inventrack_db;
USE inventrack_db;

CREATE TABLE roles (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       full_name VARCHAR(100) NOT NULL,
                       email VARCHAR(100) UNIQUE,
                       phone VARCHAR(20),
                       role_id INT NOT NULL,
                       is_active BOOLEAN DEFAULT TRUE,
                       is_approved BOOLEAN DEFAULT FALSE,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE categories (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(100) NOT NULL,
                            description TEXT
);

CREATE TABLE suppliers (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           name VARCHAR(100) NOT NULL,
                           contact_info VARCHAR(255),
                           products_supplied TEXT
);

CREATE TABLE products (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          sku VARCHAR(50) UNIQUE,
                          barcode VARCHAR(100) UNIQUE,
                          category_id INT,
                          supplier_id INT,
                          price DECIMAL(10, 2) NOT NULL,
                          cost_price DECIMAL(10, 2) NOT NULL,
                          stock_quantity INT DEFAULT 0,
                          reorder_level INT DEFAULT 10,
                          shelf_location VARCHAR(100),
                          unit_of_measure VARCHAR(20),
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          FOREIGN KEY (category_id) REFERENCES categories(id),
                          FOREIGN KEY (supplier_id) REFERENCES suppliers(id)
);

CREATE TABLE orders (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        user_id INT,
                        total_amount DECIMAL(10, 2) NOT NULL,
                        discount DECIMAL(10, 2) DEFAULT 0,
                        status ENUM('COMPLETED', 'VOIDED', 'HELD') DEFAULT 'COMPLETED',
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE order_items (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             order_id INT,
                             product_id INT,
                             quantity INT NOT NULL,
                             unit_price DECIMAL(10, 2) NOT NULL,
                             subtotal DECIMAL(10, 2) NOT NULL,
                             FOREIGN KEY (order_id) REFERENCES orders(id),
                             FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE TABLE stock_requests (
                                id INT AUTO_INCREMENT PRIMARY KEY,
                                product_id INT,
                                requested_by INT,
                                quantity INT NOT NULL,
                                status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                FOREIGN KEY (product_id) REFERENCES products(id),
                                FOREIGN KEY (requested_by) REFERENCES users(id)
);

-- Seed Data
INSERT INTO roles (name) VALUES ('Admin'), ('Cashier'), ('Stock Manager'), ('Buyer');
-- Default Admin: admin / admin123 (Needs to be hashed if we use bcrypt, for now insert raw, or we'll register one)
-- But wait, we should seed an admin via the app or write a script.