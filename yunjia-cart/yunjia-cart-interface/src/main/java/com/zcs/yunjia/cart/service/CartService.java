package com.zcs.yunjia.cart.service;

import com.zcs.yunjia.cart.pojo.CartItem;
import com.zcs.yunjia.common.pojo.RequestResult;

import java.util.List;
import java.util.Map;

public interface CartService {
    //登录状态下添加商品到购物车
    public RequestResult addItemToCart(Long itemId, Integer amount, Long userId, List<CartItem> cookieCart);
    //根据用户id取购物车
    public List<CartItem> getCartList(Long userId);
}
