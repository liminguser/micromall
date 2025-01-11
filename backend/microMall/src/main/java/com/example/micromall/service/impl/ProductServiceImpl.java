package com.example.micromall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.micromall.entity.Product;
import com.example.micromall.entity.ProductCategory;
import com.example.micromall.mapper.ProductCategoryMapper;
import com.example.micromall.mapper.ProductMapper;
import com.example.micromall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductMapper productMapper;
    
    @Autowired
    private ProductCategoryMapper categoryMapper;

    @Override
    public Page<Product> getProductList(Integer pageNum, Integer pageSize, String keyword, Long categoryId) {
        Page<Product> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        
        // 添加查询条件
        wrapper.eq(Product::getStatus, 1); // 只查询上架商品
        
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Product::getName, keyword)
                  .or()
                  .like(Product::getDescription, keyword);
        }
        
        if (categoryId != null) {
            wrapper.eq(Product::getCategoryId, categoryId);
        }
        
        // 按创建时间降序排序
        wrapper.orderByDesc(Product::getCreateTime);
        
        return productMapper.selectPage(page, wrapper);
    }

    @Override
    public Product getProductById(Long id) {
        return productMapper.selectById(id);
    }

    @Override
    public List<Product> getBannerProducts() {
        return productMapper.selectBannerProducts();
    }

    @Override
    public List<ProductCategory> getAllCategories() {
        LambdaQueryWrapper<ProductCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(ProductCategory::getSortOrder);
        return categoryMapper.selectList(wrapper);
    }
} 