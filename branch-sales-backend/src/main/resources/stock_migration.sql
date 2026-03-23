-- Central Stock Tracking (Batches from Dealers)
CREATE TABLE central_stock (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    dealer_id BIGINT,
    cost_price DOUBLE NOT NULL,
    quantity DOUBLE NOT NULL,
    remaining_quantity DOUBLE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES main_item(idmain_item),
    FOREIGN KEY (dealer_id) REFERENCES dealer(id)
);

-- Branch Stock Tracking (Batches for FIFO Sales)
CREATE TABLE branch_stock_batch (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    branch_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    purchase_price DOUBLE NOT NULL,
    quantity DOUBLE NOT NULL,
    remaining_quantity DOUBLE NOT NULL,
    received_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (branch_id) REFERENCES branches(id),
    FOREIGN KEY (product_id) REFERENCES main_item(idmain_item)
);

-- Sample Data for Verification
INSERT INTO central_stock (product_id, dealer_id, cost_price, quantity, remaining_quantity) 
VALUES (1, 1, 30.0, 100, 100);
INSERT INTO central_stock (product_id, dealer_id, cost_price, quantity, remaining_quantity) 
VALUES (1, 1, 40.0, 100, 100);
