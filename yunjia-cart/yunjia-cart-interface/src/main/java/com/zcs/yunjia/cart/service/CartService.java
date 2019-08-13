package com.zcs.yunjia.cart.service;

import com.zcs.yunjia.cart.pojo.CartItem;
import com.zcs.yunjia.common.pojo.RequestResult;

import java.util.List;
import java.util.Map;

public interface CartService {
    //登录状态下添加商品到购物车
    public RequestResult addItemToCart(Long itemId, Integer amount, Long userId, List<CartItem> cookieCart);

    //通过userId取用户的购物车
    public List<CartItem> getUserCart(Long userId);

    //合并购物车
    public List<CartItem> webMergeCart(Long uId,List<CartItem> userCart,List<CartItem> cookieCart);

    //删除商品
    public RequestResult delItemFromCart(Long userId,Long id);
}
