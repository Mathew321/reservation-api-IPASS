CREATE DATABASE restaurant_reservation;

USE restaurant_reservation;

-- CREATE TABLE customers (
--                            customer_id INT AUTO_INCREMENT PRIMARY KEY,
--                            first_name VARCHAR(50) NOT NULL,
--                            last_name VARCHAR(50) NOT NULL,
--                            email VARCHAR(100) NOT NULL UNIQUE,
--                            phone VARCHAR(15) NOT NULL,
--                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
-- );

CREATE TABLE tables (
                        table_id INT AUTO_INCREMENT PRIMARY KEY,
                        table_number VARCHAR(10) NOT NULL UNIQUE,
                        seating_capacity INT NOT NULL
);

CREATE TABLE reservations (
                              reservation_id INT AUTO_INCREMENT PRIMARY KEY,
                              table_id INT NOT NULL,
                              customer_name VARCHAR(50) NOT NULL,
                              phone VARCHAR(50) NOT NULL,
                              reservation_date DATE NOT NULL,
                              reservation_time TIME NOT NULL,
                              special_requests TEXT,
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              FOREIGN KEY (table_id) REFERENCES tables(table_id)
);

CREATE TABLE staff (
                       staff_id INT AUTO_INCREMENT PRIMARY KEY,
                       first_name VARCHAR(50) NOT NULL,
                       last_name VARCHAR(50) NOT NULL,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       phone VARCHAR(15) NOT NULL,
                       role VARCHAR(50) NOT NULL,
                       password_hash VARCHAR(255) NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE notifications (
                               notification_id INT AUTO_INCREMENT PRIMARY KEY,
                               reservation_id INT NOT NULL,
                               customer_id INT NOT NULL,
                               notification_type ENUM('Email', 'SMS') NOT NULL,
                               notification_status ENUM('Pending', 'Sent', 'Failed') NOT NULL DEFAULT 'Pending',
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               FOREIGN KEY (reservation_id) REFERENCES reservations(reservation_id),
                               FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);
