-- 插入商品分类
INSERT INTO product_category (name, sort_order) VALUES
('手机数码', 1),
('电脑办公', 2),
('家用电器', 3),
('服装鞋包', 4),
('食品生鲜', 5);

-- 插入商品数据
INSERT INTO product (category_id, name, price, stock, description, image, status, is_banner, banner_sort) VALUES
(1, 'iPhone 15 Pro', 8999.00, 100, '超强性能，专业摄影', '/uploads/iphone15.jpg', 1, 1, 1),
(1, '华为 Mate 60 Pro', 6999.00, 50, '创新科技，极致体验', '/uploads/mate60.jpg', 1, 1, 2),
(2, 'MacBook Pro', 12999.00, 30, '强大性能，专业之选', '/uploads/macbook.jpg', 1, 1, 3),
(3, '小米电视', 3999.00, 200, '超清画质，智能体验', '/uploads/tv.jpg', 1, 0, 0),
(4, '耐克运动鞋', 699.00, 300, '舒适透气，运动必备', '/uploads/nike.jpg', 1, 0, 0),
(5, '进口水果礼盒', 299.00, 150, '新鲜美味，营养健康', '/uploads/fruit.jpg', 1, 0, 0); 