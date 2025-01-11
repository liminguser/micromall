package com.example.micromall.controller;

import com.example.micromall.common.api.CommonResult;
import com.example.micromall.entity.CartItem;
import com.example.micromall.service.CartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
@Api(tags = "购物车管理")
public class CartController {
    @Autowired
    private CartService cartService;

    @ApiOperation("添加商品到购物车")
    @PostMapping("/add")
    public CommonResult<Void> addToCart(
            @RequestParam Long userId,
            @RequestParam Long productId,
            @RequestParam(defaultValue = "1") Integer quantity) {
        cartService.addToCart(userId, productId, quantity);
        return CommonResult.success(null);
    }

    @ApiOperation("更新购物车商品数量")
    @PutMapping("/update/quantity")
    public CommonResult<Void> updateQuantity(
            @RequestParam Long userId,
            @RequestParam Long productId,
            @RequestParam Integer quantity) {
        cartService.updateQuantity(userId, productId, quantity);
        return CommonResult.success(null);
    }

    @ApiOperation("删除购物车商品")
    @DeleteMapping("/delete")
    public CommonResult<Void> removeFromCart(
            @RequestParam Long userId,
            @RequestParam Long productId) {
        cartService.removeFromCart(userId, productId);
        return CommonResult.success(null);
    }

    @ApiOperation("获取购物车列表")
    @GetMapping("/list")
    public CommonResult<List<CartItem>> getCartList(@RequestParam Long userId) {
        List<CartItem> cartItems = cartService.getCartList(userId);
        return CommonResult.success(cartItems);
    }

    @ApiOperation("更新商品选中状态")
    @PutMapping("/update/selected")
    public CommonResult<Void> updateSelected(
            @RequestParam Long userId,
            @RequestParam Long productId,
            @RequestParam Boolean selected) {
        cartService.updateSelected(userId, productId, selected);
        return CommonResult.success(null);
    }

    @ApiOperation("更新所有商品选中状态")
    @PutMapping("/update/all-selected")
    public CommonResult<Void> updateAllSelected(
            @RequestParam Long userId,
            @RequestParam Boolean selected) {
        cartService.updateAllSelected(userId, selected);
        return CommonResult.success(null);
    }

    @ApiOperation("清空购物车")
    @DeleteMapping("/clear")
    public CommonResult<Void> clearCart(@RequestParam Long userId) {
        cartService.clearCart(userId);
        return CommonResult.success(null);
    }
} 