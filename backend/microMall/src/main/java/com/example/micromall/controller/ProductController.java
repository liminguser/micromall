package com.example.micromall.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.micromall.common.api.CommonResult;
import com.example.micromall.entity.Product;
import com.example.micromall.entity.ProductCategory;
import com.example.micromall.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@Api(tags = "商品管理")
public class ProductController {
    @Autowired
    private ProductService productService;

    @ApiOperation("获取商品列表")
    @GetMapping
    public CommonResult<Page<Product>> getProductList(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam("搜索关键词") @RequestParam(required = false) String keyword,
            @ApiParam("分类ID") @RequestParam(required = false) Long categoryId) {
        Page<Product> productList = productService.getProductList(pageNum, pageSize, keyword, categoryId);
        return CommonResult.success(productList);
    }

    @ApiOperation("获取商品详情")
    @GetMapping("/{id}")
    public CommonResult<Product> getProductDetail(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return CommonResult.success(product);
    }

    @ApiOperation("获取轮播图商品")
    @GetMapping("/banners")
    public CommonResult<List<Product>> getBannerProducts() {
        List<Product> bannerProducts = productService.getBannerProducts();
        return CommonResult.success(bannerProducts);
    }

    @ApiOperation("获取所有商品分类")
    @GetMapping("/categories")
    public CommonResult<List<ProductCategory>> getAllCategories() {
        List<ProductCategory> categories = productService.getAllCategories();
        return CommonResult.success(categories);
    }
} 