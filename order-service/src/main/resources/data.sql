INSERT INTO orders (total_price, order_status) VALUES
(100.50,'PENDING'),
(200.75,'CONFIRMED'),
(150.25,'DELIVERED'),
(310.00,'CANCELLED'),
(400.00,'PENDING'),
(230.00,'CONFIRMED'),
(260.00,'CANCELLED'),
(345.00,'PENDING'),
(423.00,'DELIVERED');
INSERT INTO order_item (order_id,product_id, quantity) VALUES
(1,101,2),
(1,102,2),
(2,103,3),
(2,104,4),
(3,105,3),
(3,106,4),
(4,107,2),
(4,108,1);