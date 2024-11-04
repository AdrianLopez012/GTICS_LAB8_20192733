
CREATE SCHEMA IF NOT EXISTS unlp_events;
USE unlp_events;
CREATE TABLE IF NOT EXISTS categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);
CREATE TABLE IF NOT EXISTS events (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    date DATE NOT NULL,
    category_id INT NOT NULL,
    max_capacity INT NOT NULL CHECK (max_capacity >= 1),
    current_reservations INT NOT NULL DEFAULT 0 CHECK (current_reservations >= 0),
    FOREIGN KEY (category_id) REFERENCES categories(id)
);
CREATE TABLE IF NOT EXISTS reservations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    event_id INT NOT NULL,
    reserver_name VARCHAR(100) NOT NULL,
    reserver_email VARCHAR(100) NOT NULL,
    number_of_seats INT NOT NULL CHECK (number_of_seats > 0),
    FOREIGN KEY (event_id) REFERENCES events(id)
);
