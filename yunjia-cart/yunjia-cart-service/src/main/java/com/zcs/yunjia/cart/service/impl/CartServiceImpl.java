package com.zcs.yunjia.cart.service.impl;

import com.zcs.yunjia.cart.pojo.CartItem;
import com.zcs.yunjia.cart.redis.JedisClient;
import com.zcs.yunjia.cart.service.CartService;
import com.zcs.yunjia.common.pojo.RequestResult;
import com.zcs.yunjia.mapper.TbItemMapper;
import com.zcs.yunjia.pojo.TbItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private TbItemMapper itemMapper;
    @Autowired
    private JedisClient jedisClient;
    @Value("{SSO_REDIS_TOKEN_KEY}")
    private String SSO_REDIS_TOKEN_KEY;

    /**
     * @param itemId 商品id
     * @param amount  商品数量
     * @param userId  用户id
     * @param cookieCart  cookie中的购物车 没有传递null
     * @return
     */
    public RequestResult addItemToCart(Long itemId, Integer amount,Long userId,List<CartItem> cookieCart) {
        //合并购物车
        if(cookieCart != null){

        }
        return null;
    }

    /**
     * 根据id查询商品信息
     * @param id
     * @return 商品信息
     */
    public TbItem getItemById(Long id){
        return itemMapper.selectByPrimaryKey(id);
    }

    public Cookie getItemFromCookie(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        return null;
    }
}
