-- Migration script to support versioned sync and branch-specific pricing

-- 1. Add version and updated_at columns to Main Items
ALTER TABLE main_item ADD COLUMN version BIGINT DEFAULT 0;
ALTER TABLE main_item ADD COLUMN updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;

-- 2. Create or update Branch Product Prices table
CREATE TABLE IF NOT EXISTS branch_product_price (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    branch_id BIGINT NOT NULL,
    price DOUBLE NOT NULL,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0,
    UNIQUE (product_id, branch_id),
    FOREIGN KEY (product_id) REFERENCES main_item(idmain_item)
);

-- Ensure updated_at exists if table was already created
-- ALTER TABLE branch_product_price ADD COLUMN updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;

-- 3. Create Change Log table for incremental sync (optional backup)
CREATE TABLE IF NOT EXISTS change_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    branch_id BIGINT,
    entity_type VARCHAR(50),
    entity_id BIGINT,
    operation VARCHAR(20),
    version BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 4. Create Branch Sync Status table
CREATE TABLE IF NOT EXISTS branch_sync_status (
    branch_id BIGINT PRIMARY KEY,
    last_sync_time DATETIME NOT NULL
);

-- 5. Incoming Invoice Event table (Phase 1)
CREATE TABLE IF NOT EXISTS incoming_invoice_event (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sale_uuid VARCHAR(100) UNIQUE NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 6. Cloud Change Log table (Phase 1)
CREATE TABLE IF NOT EXISTS cloud_change_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    table_name VARCHAR(100) NOT NULL,
    record_id BIGINT NOT NULL,
    action VARCHAR(20) NOT NULL,
    data JSON NOT NULL,
    branch_id BIGINT NULL,
    sequence_no BIGINT UNIQUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
