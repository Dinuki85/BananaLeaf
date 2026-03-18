-- Migration script for branch-wise product pricing and availability

CREATE TABLE IF NOT EXISTS branch_products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    branch_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    branch_price DECIMAL(10, 2),
    is_available BOOLEAN DEFAULT TRUE,
    is_price_updated BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY unique_branch_product (branch_id, product_id),
    INDEX idx_branch_id (branch_id),
    INDEX idx_product_id (product_id),
    CONSTRAINT fk_branch_products_branch FOREIGN KEY (branch_id) REFERENCES branches(id),
    CONSTRAINT fk_branch_products_product FOREIGN KEY (product_id) REFERENCES main_item(idmain_item)
);

-- Optional: Migrate existing branch prices if needed
-- INSERT INTO branch_products (branch_id, product_id, branch_price, is_available, is_price_updated)
-- SELECT branch_id, product_id, price, TRUE, TRUE FROM branch_product_price;
