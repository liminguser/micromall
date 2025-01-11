package com.example.micromall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.micromall.entity.CartItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CartItemMapper extends BaseMapper<CartItem> {
    @Select("SELECT c.*, " +
            "p.name as 'product.name', " +
            "p.price as 'product.price', " +
            "p.description as 'product.description', " +
            "p.image as 'product.image', " +
            "p.stock as 'product.stock' " +
            "FROM cart_item c " +
            "LEFT JOIN product p ON c.product_id = p.id " +
            "WHERE c.user_id = #{userId}")
    List<CartItem> selectCartItemsWithProduct(Long userId);
} 