CREATE TABLE dealer (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    is_active BOOLEAN DEFAULT TRUE
);

-- Sample Data
INSERT INTO dealer (name, phone, is_active) VALUES ('Colombo Traders', '0112223334', TRUE);
INSERT INTO dealer (name, phone, is_active) VALUES ('Kandy Suppliers', '0812223334', TRUE);
INSERT INTO dealer (name, phone, is_active) VALUES ('Galle Distributors', '0912223334', FALSE);
