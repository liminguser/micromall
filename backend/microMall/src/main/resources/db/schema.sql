-- 创建数据库
CREATE DATABASE IF NOT EXISTS micromall DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE micromall;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username` varchar(64) NOT NULL COMMENT '用户名',
    `password` varchar(128) NOT NULL COMMENT '密码',
    `nickname` varchar(64) DEFAULT NULL COMMENT '昵称',
    `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
    `email` varchar(64) DEFAULT NULL COMMENT '邮箱',
    `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
    `gender` tinyint(4) DEFAULT '0' COMMENT '性别：0未知，1男，2女',
    `birthday` datetime DEFAULT NULL COMMENT '生日',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除：0否，1是',
    `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态：0禁用，1启用',
    `roles` varchar(128) DEFAULT NULL COMMENT '角色',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表'; 

-- 商品分类表
CREATE TABLE IF NOT EXISTS product_category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL COMMENT '分类名称',
    sort_order INT DEFAULT 0 COMMENT '排序',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '商品分类表';

-- 商品表
CREATE TABLE IF NOT EXISTS product (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    category_id BIGINT NOT NULL COMMENT '分类ID',
    name VARCHAR(100) NOT NULL COMMENT '商品名称',
    price DECIMAL(10,2) NOT NULL COMMENT '商品价格',
    stock INT NOT NULL DEFAULT 0 COMMENT '库存数量',
    description TEXT COMMENT '商品描述',
    image VARCHAR(255) COMMENT '商品图片URL',
    status TINYINT DEFAULT 1 COMMENT '状态：0-下架，1-上架',
    is_banner TINYINT DEFAULT 0 COMMENT '是否为轮播图商品：0-否，1-是',
    banner_sort INT DEFAULT 0 COMMENT '轮播图排序',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES product_category(id)
) COMMENT '商品表'; 

-- 购物车表
CREATE TABLE IF NOT EXISTS cart_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    quantity INT NOT NULL DEFAULT 1 COMMENT '商品数量',
    selected TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否选中：0否，1是',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (product_id) REFERENCES product(id),
    UNIQUE KEY uk_user_product (user_id, product_id) COMMENT '同一用户的同一商品只能有一条记录'
) COMMENT '购物车表'; 