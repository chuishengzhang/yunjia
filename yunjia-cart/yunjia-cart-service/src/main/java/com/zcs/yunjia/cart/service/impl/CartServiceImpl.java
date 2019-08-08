package com.zcs.yunjia.cart.service.impl;

import com.zcs.yunjia.cart.pojo.CartItem;
import com.zcs.yunjia.cart.redis.JedisClient;
import com.zcs.yunjia.cart.service.CartService;
import com.zcs.yunjia.common.pojo.RequestResult;
import com.zcs.yunjia.common.utils.JsonUtils;
import com.zcs.yunjia.mapper.TbItemMapper;
import com.zcs.yunjia.pojo.TbItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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
        RequestResult result = new RequestResult();
        try {
           //创建用户购物车
           List<CartItem> userCart = null;
           //判断该用户是否已存在购物车
           String userCartJson = jedisClient.get(userId+"");
           userCart = userCartJson == null ? new ArrayList<CartItem>() : JsonUtils.jsonToList(userCartJson, CartItem.class);
           //将商品添加到用户购物车
           for (int i = 0; i < userCart.size(); i++) {
               CartItem ci = userCart.get(i);
               if (itemId.equals(ci.getId())) {
                   System.out.println("user购物车与用户购物车有相同商品"+ci);
                   ci.setNum(amount + ci.getNum());
               }
           }
           //查询商品信息 封装到购物车商品
           TbItem item = itemMapper.selectByPrimaryKey(itemId);
           CartItem cartItem = new CartItem();
           cartItem.setId(itemId);
           cartItem.setNum(amount);
           cartItem.setImage(item.getImage());
           cartItem.setTitle(item.getTitle());
           cartItem.setPrice(item.getPrice());
           //添加到用户购物车
           userCart.add(cartItem);
           //合并购物车
            System.out.println(cookieCart.size()+"!");
           if (cookieCart != null) {
               for (int i = 0; i < userCart.size(); i++) {
                   for (int j = 0; j < cookieCart.size(); j++) {
                       System.out.println(userCart.get(i).getTitle()+"::"+cookieCart.get(j).getTitle());
                       if (cookieCart.get(j).getId().equals(userCart.get(i).getId())) {
                           System.out.println("xiangtong:"+userCart.get(i).getTitle());
                           userCart.get(i).setNum(cookieCart.get(j).getNum() + userCart.get(i).getNum());
                           break;
                       } else {
                           userCart.add(cookieCart.get(j));
                           break;
                       }
                   }
               }
           }
           //持久化到redis
            jedisClient.set(userId+"",JsonUtils.objectToJson(userCart));
            result.setStatus(200);
            result.setData(userCart);
       }catch (Exception e) {
            e.printStackTrace();
            result.setStatus(400);
            result.setData(null);
        }
        return result;
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
    public List<CartItem> getCart(Long userId){
        //查询登录用户的id
        return JsonUtils.jsonToList(jedisClient.get(userId+""),CartItem.class);
    }
}
