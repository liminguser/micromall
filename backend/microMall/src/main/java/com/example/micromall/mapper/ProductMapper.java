package com.example.micromall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.micromall.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
    @Select("SELECT * FROM product WHERE is_banner = 1 ORDER BY banner_sort")
    List<Product> selectBannerProducts();
} 