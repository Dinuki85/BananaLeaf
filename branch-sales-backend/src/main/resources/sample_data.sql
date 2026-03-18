-- Sample Data Script for Pharmacy/Inventory Management System

-- 1. Insert Main Category
INSERT IGNORE INTO main_category (idmain_category, name) VALUES (1, 'General Pharmaceuticals');

-- 2. Insert Branches
INSERT IGNORE INTO branches (id, name, location, status) VALUES 
(1, 'Colombo Branch', 'Colombo 03', 1),
(2, 'Kandy Branch', 'Kandy City', 1),
(3, 'Galle Branch', 'Galle Fort', 1),
(4, 'Jaffna Branch', 'Jaffna Town', 1);

-- 3. Insert Products (MainItem)
INSERT IGNORE INTO main_item (idmain_item, main_category_idmain_category, name, code, up, sp, active) VALUES 
(1, 1, 'Paracetamol 500mg', 'MED-001', 5.00, 10.00, '1'),
(2, 1, 'Insulin Glargine', 'MED-002', 200.00, 350.00, '1'),
(3, 1, 'Aspirin 75mg', 'MED-003', 2.00, 5.00, '1'),
(4, 1, 'Amoxicillin 250mg', 'MED-004', 15.00, 30.00, '1'),
(5, 1, 'Ibuprofen 400mg', 'MED-005', 8.00, 15.00, '1'),
(6, 1, 'Metformin 500mg', 'MED-006', 10.00, 20.00, '1'),
(7, 1, 'Atorvastatin 10mg', 'MED-007', 25.00, 45.00, '1'),
(8, 1, 'Omeprazole 20mg', 'MED-008', 12.00, 25.00, '1'),
(9, 1, 'Amlodipine 5mg', 'MED-009', 18.00, 35.00, '1'),
(10, 1, 'Cetirizine 10mg', 'MED-010', 4.00, 8.00, '1');

-- 4. Insert Branch Products (Initial setup with default prices)
INSERT IGNORE INTO branch_products (branch_id, product_id, branch_price, is_available, is_price_updated)
SELECT b.id, m.idmain_item, m.sp, TRUE, FALSE
FROM branches b
CROSS JOIN main_item m;

-- 5. Insert Invoices (10 sample orders)
INSERT IGNORE INTO invoice (idinvoice, branch_id, date, amount, total, payment_type, status) VALUES 
(1, 1, '2026-03-18', 100.00, 100.00, 'CASH', 'PAID'),
(2, 1, '2026-03-18', 250.00, 250.00, 'CARD', 'PAID'),
(3, 2, '2026-03-18', 150.00, 150.00, 'CASH', 'PAID'),
(4, 2, '2026-03-18', 300.00, 300.00, 'CASH', 'PAID'),
(5, 3, '2026-03-18', 450.00, 450.00, 'CARD', 'PAID'),
(6, 3, '2026-03-18', 120.00, 120.00, 'CASH', 'PAID'),
(7, 4, '2026-03-18', 500.00, 500.00, 'CARD', 'PAID'),
(8, 4, '2026-03-18', 80.00, 80.00, 'CASH', 'PAID'),
(9, 1, '2026-03-18', 220.00, 220.00, 'CARD', 'PAID'),
(10, 2, '2026-03-18', 190.00, 190.00, 'CASH', 'PAID');

-- 6. Insert Invoice Items (invoice_has_main_item)
-- id_main_item_has_invoice, invoice_idinvoice, branch_id, main_item_idmain_item, qty, up, sp
INSERT IGNORE INTO invoice_has_main_item (id_main_item_has_invoice, invoice_idinvoice, branch_id, main_item_idmain_item, qty, up, sp) VALUES 
(1, 1, 1, 1, 10, 5.00, 10.00),
(2, 2, 1, 2, 1, 200.00, 250.00),
(3, 3, 2, 3, 30, 2.00, 5.00),
(4, 4, 2, 4, 10, 15.00, 30.00),
(5, 5, 3, 5, 30, 8.00, 15.00),
(6, 6, 3, 6, 6, 10.00, 20.00),
(7, 7, 4, 7, 10, 25.00, 50.00),
(8, 8, 4, 8, 3, 12.00, 25.00),
(9, 9, 1, 9, 5, 18.00, 35.00),
(10, 10, 2, 10, 20, 4.00, 8.00);
