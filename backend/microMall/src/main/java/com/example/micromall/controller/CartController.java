package com.example.micromall.controller;

import com.example.micromall.common.api.CommonResult;
import com.example.micromall.entity.CartItem;
import com.example.micromall.service.CartService;
import com.example.micromall.security.UserDetailsImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
@Api(tags = "购物车管理")
public class CartController {
    @Autowired
    private CartService cartService;

    // 获取当前用户ID
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return userDetails.getId();
        }
        throw new RuntimeException("用户未登录");
    }

    @ApiOperation("添加商品到购物车")
    @PostMapping("/add")
    public CommonResult<Void> addToCart(
            @RequestBody CartRequest request) {
        cartService.addToCart(getCurrentUserId(), request.getProductId(), request.getQuantity());
        return CommonResult.success(null);
    }

    @ApiOperation("更新购物车商品数量")
    @PutMapping("/update/quantity")
    public CommonResult<Void> updateQuantity(
            @RequestBody CartRequest request) {
        cartService.updateQuantity(getCurrentUserId(), request.getProductId(), request.getQuantity());
        return CommonResult.success(null);
    }

    @ApiOperation("删除购物车商品")
    @DeleteMapping("/delete")
    public CommonResult<Void> removeFromCart(@RequestParam Long productId) {
        cartService.removeFromCart(getCurrentUserId(), productId);
        return CommonResult.success(null);
    }

    @ApiOperation("获取购物车列表")
    @GetMapping("/list")
    public CommonResult<List<CartItem>> getCartList() {
        List<CartItem> cartItems = cartService.getCartList(getCurrentUserId());
        return CommonResult.success(cartItems);
    }

    @ApiOperation("更新商品选中状态")
    @PutMapping("/update/selected")
    public CommonResult<Void> updateSelected(
            @RequestBody CartSelectedRequest request) {
        cartService.updateSelected(getCurrentUserId(), request.getProductId(), request.getSelected());
        return CommonResult.success(null);
    }

    @ApiOperation("更新所有商品选中状态")
    @PutMapping("/update/all-selected")
    public CommonResult<Void> updateAllSelected(@RequestBody CartAllSelectedRequest request) {
        cartService.updateAllSelected(getCurrentUserId(), request.getSelected());
        return CommonResult.success(null);
    }

    @ApiOperation("清空购物车")
    @DeleteMapping("/clear")
    public CommonResult<Void> clearCart() {
        cartService.clearCart(getCurrentUserId());
        return CommonResult.success(null);
    }
}

// 请求体类
class CartRequest {
    private Long productId;
    private Integer quantity;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}

class CartSelectedRequest {
    private Long productId;
    private Boolean selected;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}

class CartAllSelectedRequest {
    private Boolean selected;

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
} 