package com.example.micromall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.micromall.entity.Product;
import com.example.micromall.entity.ProductCategory;

import java.util.List;

public interface ProductService {
    // 获取商品列表
    Page<Product> getProductList(Integer pageNum, Integer pageSize, String keyword, Long categoryId);
    
    // 获取商品详情
    Product getProductById(Long id);
    
    // 获取轮播图商品
    List<Product> getBannerProducts();
    
    // 获取所有商品分类
    List<ProductCategory> getAllCategories();
} 