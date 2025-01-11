package com.example.micromall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("product")
public class Product {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long categoryId;
    private String name;
    private BigDecimal price;
    private Integer stock;
    private String description;
    private String image;
    private Integer status;
    private Integer isBanner;
    private Integer bannerSort;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
} 