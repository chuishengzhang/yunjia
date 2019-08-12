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
import java.util.Map;


@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private TbItemMapper itemMapper;
    @Autowired
    private JedisClient jedisClient;
    @Value("{SSO_REDIS_TOKEN_KEY}")
    private String SSO_REDIS_TOKEN_KEY;

    /**
     * 查询用户购物车内指定id的信息
     * @param userId  用户信息
     * @param itemId  需要查询商品的id
     * @return 商品信息 不存在返回null
     */
    public CartItem getCartItemById(Long userId,Long itemId){
        List<CartItem> userCart = getUserCart(userId);
        CartItem cartItem = null;
        for(CartItem ci : userCart){
            if(ci.getId() == itemId){
                cartItem = ci;
            }
        }
        return cartItem;
    }
    /**
     * 登录状态下添加商品到购物车
     * @param itemId  添加商品的id
     * @param amount  添加商品的数量
     * @param userId  登录用户的id
     * @param cookieCart cookie中的购物车 没有则传null
     * @return  RequestResult data是用户购物车 state: 200表示不需要清空购物车 201需要清空购物车
     */
    public RequestResult addItemToCart(Long itemId, Integer amount,Long userId,List<CartItem> cookieCart) {
        RequestResult result = new RequestResult();
        int state = 200;
        List<CartItem> userCart = null;
        try {
            //获取商品并封装成CartItem
            TbItem item = itemMapper.selectByPrimaryKey(itemId);
            CartItem cartItem = new CartItem();
            cartItem.setId(itemId);
            cartItem.setNum(amount);
            cartItem.setImage(item.getImage());
            cartItem.setTitle(item.getTitle());
            cartItem.setPrice(item.getPrice());
            //获取用户购物车
            userCart = getUserCart(userId);
            if(userCart.size() != 0){
                //用户购物车存在商品
                if(cookieCart != null){
                    //cookie购物车有商品 需要合并购物车
                    userCart = mergeCart(userId,userCart,cookieCart);
                    state = 201;
                    System.out.println("user不为空，cookie不为空");
                }
                for(CartItem ci : userCart){
                    System.out.println(ci.getId()+"::"+itemId);
                    if(ci.getId().equals(itemId)){
                        ci.setNum(ci.getNum()+amount);
                        System.out.println(ci.getId()+":..."+ci.getNum());
                    }
                }
                userCart.add(cartItem);
                //持久化到redis
                toRedis(userId,userCart);
            }else{
                //用户购物车不存在商品
                if(cookieCart != null){
                    //用户购物车不存在 cookie购物车存在
                    cookieCart.add(cartItem);
                    toRedis(userId,cookieCart);
                    state = 201;
                    System.out.println("user为空，cookie不为空");
                }else{
                    System.out.println("都为空");
                    //jedisClient.hSet(userId+"",itemId+"",null);
                }
            }
            System.out.println("before:"+state);
            result.setStatus(state);
            result.setData(userCart);
            System.out.println(result.getStatus()+result.getData().toString());
            return result;
        }catch (Exception e){
            System.out.println("异常了");
            e.printStackTrace();
            result.setData(state);
            result.setData(null);
            return result;
        }
    }
    /**
     * 从redis取用户数据库
     * @param userId 用户id
     * @return 用户购物车list 不存在则返回空的集合
     */
    public List<CartItem> getUserCart(Long userId){
        List<CartItem> userCart = null;
        try{
            Map<String,String> userCartMap = jedisClient.hGetAll(userId+"");
             userCart = new ArrayList<CartItem>();
            //将map转成list
            for (Map.Entry<String,String> entry : userCartMap.entrySet()) {
                String ciJson = entry.getValue();
                userCart.add(JsonUtils.jsonToPojo(ciJson,CartItem.class));
            }
        }catch (Exception e){
            e.printStackTrace();
            return new ArrayList<CartItem>();
        }
        return userCart;
    }
    /**
     * 根据用户id和商品id查询该用户购物车是否存在该商品
     * @param userId  用户id
     * @param itemId   商品id
     * @return 存在返回true 不存在返回false
     */
    public boolean hasCartItem(Long userId,Long itemId){
        String value;
        try{
            value = jedisClient.hGet(userId+"",itemId+"");
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return value == null ? false : true;
    }
    /**
     * 合并购物车
     * @param uId 用户id
     * @param userCart 用户购物车
     * @param cookieCart cookie购物车
     * @return 合并后的客户购物车
     */
    public List<CartItem> mergeCart(Long uId,List<CartItem> userCart,List<CartItem> cookieCart){
        for(CartItem ci : cookieCart){
            if(hasCartItem(uId,ci.getId())){
                //cookie中存在购物车已存在的商品
                for(CartItem c : userCart){
                    if(c.getId() == ci.getId()){
                        //循环遍历用户购物车 找到该商品修改数量
                        c.setNum(c.getNum()+ci.getNum());
                    }
                }
            }else{
                //cookie不存在用户购物车的商品 直接将cookie商品添加到用户购物车
                userCart.add(ci);
            }
        }
        return userCart;
    }
    /**
     *
     * @param uId
     * @param userCart
     * @param cookieCart
     * @return
     */
    public List<CartItem> webMergeCart(Long uId,List<CartItem> userCart,List<CartItem> cookieCart){
       List<CartItem> cart = mergeCart(uId,userCart,cookieCart);
        toRedis(uId,cart);
        return cart;
    }
    /**
     * 根据用户id和购物车列表将数据持久化到redis
     * @param userId 用户id
     * @param cart 购物车数据
     */
    public void toRedis(Long userId,List<CartItem> cart){
        for(CartItem ci : cart){
            jedisClient.hSet(userId+"",ci.getId()+"",JsonUtils.objectToJson(ci));
        }
    }
}
