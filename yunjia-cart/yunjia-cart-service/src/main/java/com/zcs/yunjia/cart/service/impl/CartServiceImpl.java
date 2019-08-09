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
     * @param itemId 商品id
     * @param amount  商品数量
     * @param userId  用户id
     * @param cookieCart  cookie中的购物车 没有传递null
     * @return
     */
    /*public RequestResult addItemToCart(Long itemId, Integer amount,Long userId,List<CartItem> cookieCart) {
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
               if(userCart.size() == 0){
                   userCart.add(cartItem);
               }
               boolean flag = true;//是否可添加到用户购物车标记
               for (int i = 0; i < userCart.size(); i++) {
                   for (int j = 0; j < cookieCart.size(); j++) {
                       System.out.println(userCart.get(i).getTitle()+"::"+cookieCart.get(j).getTitle());
                       if (cookieCart.get(j).getId().equals(userCart.get(i).getId())) {
                           System.out.println("xiangtong:"+userCart.get(i).getTitle());
                           userCart.get(i).setNum(cookieCart.get(j).getNum() + userCart.get(i).getNum());
                       } else {
                           if(flag) {
                               userCart.add(cookieCart.get(j));
                               flag = false;
                           }
                       }
                   }
               }
           }
           //持久化到redis
            System.out.println(userCart.size()+"reids before");
            jedisClient.set(userId+"",JsonUtils.objectToJson(userCart));
            result.setStatus(200);
            result.setData(userCart);
       }catch (Exception e) {
            e.printStackTrace();
            result.setStatus(400);
            result.setData(null);
        }
        return result;
    }*/

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
        boolean clearCookieCart = false;
        int state = 460;
        List<CartItem> userCart = null;
        try {
            //获取用户购物车
            userCart = getUserCart(userId);
            //获取商品并封装成CartItem
            TbItem item = itemMapper.selectByPrimaryKey(itemId);
            CartItem cartItem = new CartItem();
            cartItem.setId(itemId);
            cartItem.setNum(amount);
            cartItem.setImage(item.getImage());
            cartItem.setTitle(item.getTitle());
            cartItem.setPrice(item.getPrice());
            //判断用户购物车是否存在该商品
            if (hasCartItem(userId, itemId)) {
                //存在 数量相加
                cartItem.setNum(item.getNum() + amount);
            } else {
                //不存在  直接添加
                cookieCart.add(cartItem);
            }
            //cookie购物车不为空 合并购物车
            if (cookieCart != null && userCart != null) {
                for (CartItem ci : cookieCart) {
                    if (hasCartItem(userId, ci.getId())) {
                        //当前cookie中商品在用户购物车中已存在
                        //用户购物车中对应cookie商品的数量
                        int cartNum = getCartItemById(userId, ci.getId()).getNum();
                        //数量相加
                        ci.setNum(ci.getNum() + cartNum);
                    } else {
                        //当前cookie中商品不存在用户购物车中
                        userCart.add(ci);
                    }
                }
                //合并完购物车之后清空cookie购物车
                clearCookieCart = true;
            }
            //是否需要清空cookie购物车 201需要  200不需要
            state = clearCookieCart == true ? 201 : 200;
        }catch (Exception e){
            e.printStackTrace();
            result.setData(state);
            result.setData(null);
            return result;
        }
        result.setData(state);
        result.setData(userCart);
        return result;
    }


    public List<CartItem> getCartList(Long userId){
        List<CartItem> result;
        try{
            result = JsonUtils.jsonToList(jedisClient.get(userId+""),CartItem.class);
        }catch (Exception e){
            e.printStackTrace();
            return  null;
        }
        return result;
    }

    /**
     * 从redis取用户数据库
     * @param userId 用户id
     * @return 用户购物车list 不存在则返回空的集合
     */
    public List<CartItem> getUserCart(Long userId){
        try{
            Map<String,String> userCartMap = jedisClient.hGetAll(userId+"");
            List<CartItem> userCart = new ArrayList<CartItem>();
            //将map转成list
            for (Map.Entry<String,String> entry : userCartMap.entrySet()) {
                userCart.add(JsonUtils.jsonToPojo(entry.getValue(),CartItem.class));
            }
        }catch (Exception e){
            e.printStackTrace();
            return new ArrayList<CartItem>();
        }
        return new ArrayList<CartItem>();
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
}
