package com.example.micromall.service;

import com.example.micromall.entity.CartItem;

import java.util.List;

public interface CartService {
    // 添加商品到购物车
    void addToCart(Long userId, Long productId, Integer quantity);
    
    // 更新购物车商品数量
    void updateQuantity(Long userId, Long productId, Integer quantity);
    
    // 从购物车删除商品
    void removeFromCart(Long userId, Long productId);
    
    // 获取用户的购物车列表
    List<CartItem> getCartList(Long userId);
    
    // 更新商品选中状态
    void updateSelected(Long userId, Long productId, Boolean selected);
    
    // 更新所有商品的选中状态
    void updateAllSelected(Long userId, Boolean selected);
    
    // 清空购物车
    void clearCart(Long userId);
} 