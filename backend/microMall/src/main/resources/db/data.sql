-- 插入商品分类数据
INSERT INTO product_category (id, name, sort_order) VALUES
(1, '手机数码', 1),
(2, '电脑办公', 2),
(3, '家用电器', 3),
(4, '服装配饰', 4);

-- 插入商品数据
INSERT INTO product (category_id, name, price, stock, description, image, status, is_banner, banner_sort) VALUES
(1, 'iPhone 14 Pro', 7999.00, 100, '超视网膜XDR显示屏，A16仿生芯片', 'https://picsum.photos/800/600?random=1', 1, 1, 1),
(2, 'MacBook Pro', 12999.00, 50, 'M2芯片，超长续航', 'https://picsum.photos/800/600?random=2', 1, 1, 2),
(1, '索尼耳机', 1999.00, 200, '主动降噪，高解析音质', 'https://picsum.photos/800/600?random=3', 1, 1, 3),
(2, '机械键盘', 499.00, 300, '机械轴体，RGB背光', 'https://picsum.photos/800/600?random=4', 1, 0, 0),
(2, '游戏鼠标', 299.00, 400, '16000DPI，可编程按键', 'https://picsum.photos/800/600?random=5', 1, 0, 0),
(3, '显示器', 2499.00, 80, '4K分辨率，HDR技术', 'https://picsum.photos/800/600?random=6', 1, 0, 0),
(1, '智能手表', 1599.00, 150, '心率监测，运动追踪', 'https://picsum.photos/800/600?random=7', 1, 0, 0),
(3, '蓝牙音箱', 699.00, 200, '360度环绕音效，防水设计', 'https://picsum.photos/800/600?random=8', 1, 0, 0),
(2, '平板电脑', 3999.00, 100, '120Hz高刷新率，手写笔支持', 'https://picsum.photos/800/600?random=9', 1, 0, 0),
(3, '无线充电器', 199.00, 500, '15W快充，多设备支持', 'https://picsum.photos/800/600?random=10', 1, 0, 0); 