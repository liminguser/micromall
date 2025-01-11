package com.example.micromall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.micromall.entity.CartItem;
import com.example.micromall.entity.Product;
import com.example.micromall.mapper.CartItemMapper;
import com.example.micromall.mapper.ProductMapper;
import com.example.micromall.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartItemMapper cartItemMapper;
    
    @Autowired
    private ProductMapper productMapper;

    @Override
    @Transactional
    public void addToCart(Long userId, Long productId, Integer quantity) {
        // 检查商品是否存在
        Product product = productMapper.selectById(productId);
        if (product == null || product.getStatus() == 0) {
            throw new RuntimeException("商品不存在或已下架");
        }
        
        // 检查库存
        if (product.getStock() < quantity) {
            throw new RuntimeException("商品库存不足");
        }
        
        // 查找购物车中是否已存在该商品
        LambdaQueryWrapper<CartItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CartItem::getUserId, userId)
                   .eq(CartItem::getProductId, productId);
        CartItem existingItem = cartItemMapper.selectOne(queryWrapper);
        
        if (existingItem != null) {
            // 更新数量
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            cartItemMapper.updateById(existingItem);
        } else {
            // 新增购物车项
            CartItem cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(productId);
            cartItem.setQuantity(quantity);
            cartItem.setSelected(true);
            cartItemMapper.insert(cartItem);
        }
    }

    @Override
    @Transactional
    public void updateQuantity(Long userId, Long productId, Integer quantity) {
        // 检查商品库存
        Product product = productMapper.selectById(productId);
        if (product == null || product.getStatus() == 0) {
            throw new RuntimeException("商品不存在或已下架");
        }
        if (product.getStock() < quantity) {
            throw new RuntimeException("商品库存不足");
        }
        
        // 更新数量
        LambdaUpdateWrapper<CartItem> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(CartItem::getUserId, userId)
                    .eq(CartItem::getProductId, productId)
                    .set(CartItem::getQuantity, quantity);
        cartItemMapper.update(null, updateWrapper);
    }

    @Override
    @Transactional
    public void removeFromCart(Long userId, Long productId) {
        LambdaQueryWrapper<CartItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CartItem::getUserId, userId)
                   .eq(CartItem::getProductId, productId);
        cartItemMapper.delete(queryWrapper);
    }

    @Override
    public List<CartItem> getCartList(Long userId) {
        return cartItemMapper.selectCartItemsWithProduct(userId);
    }

    @Override
    @Transactional
    public void updateSelected(Long userId, Long productId, Boolean selected) {
        LambdaUpdateWrapper<CartItem> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(CartItem::getUserId, userId)
                    .eq(CartItem::getProductId, productId)
                    .set(CartItem::getSelected, selected);
        cartItemMapper.update(null, updateWrapper);
    }

    @Override
    @Transactional
    public void updateAllSelected(Long userId, Boolean selected) {
        LambdaUpdateWrapper<CartItem> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(CartItem::getUserId, userId)
                    .set(CartItem::getSelected, selected);
        cartItemMapper.update(null, updateWrapper);
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {
        LambdaQueryWrapper<CartItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CartItem::getUserId, userId);
        cartItemMapper.delete(queryWrapper);
    }
} 