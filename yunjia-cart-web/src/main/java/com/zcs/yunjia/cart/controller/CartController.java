package com.zcs.yunjia.cart.controller;

import com.zcs.yunjia.cart.pojo.CartItem;
import com.zcs.yunjia.cart.service.CartService;
import com.zcs.yunjia.common.pojo.RequestResult;
import com.zcs.yunjia.common.utils.CookieUtils;
import com.zcs.yunjia.common.utils.JsonUtils;
import com.zcs.yunjia.item.service.ItemService;
import com.zcs.yunjia.pojo.TbItem;
import com.zcs.yunjia.pojo.TbUser;
import com.zcs.yunjia.sso.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


@Controller
public class CartController {

    @Autowired
    private ItemService itemService;
    @Autowired
    private CartService cartService;
    @Autowired
    private UserService userService;

    private HashMap<String,String> cookies = new HashMap<String,String>();

    @RequestMapping(value = {"/","/cart"})
    public String showCart(Model model,HttpServletRequest request,HttpServletResponse response){
        try{
            List<CartItem> cart = null;
            String cookieCart = CookieUtils.getCookieValue(request,"cartList");
            String token = CookieUtils.getCookieValue(request,"userToken");
            RequestResult result = userService.checkToken(token);
            if(result.getStatus() == 200){
                TbUser user = (TbUser)result.getData();
                //已登录 从redis去购物车
                //判断是否有cookie购物车
                if(cookieCart != null && !"".equals(cookieCart)){
                    //存在cookie 调用服务合并购物车
                    cart = cartService.webMergeCart(user.getId(),cartService.getUserCart(user.getId()),JsonUtils.jsonToList(cookieCart,CartItem.class));
                    CookieUtils.deleteCookie(request,response,"cartList");
                }else {
                    //不存在 直接去redis购物车
                    cart = cartService.getUserCart(user.getId());
                    System.out.println("redis");
                    if (cart.size() == 0) {
                        cart = cookieCart == null ? null : JsonUtils.jsonToList(cookieCart, CartItem.class);
                        System.out.println("Redis:cookie");
                    }
                }
            }else{
                //未登录 取cookie购物车 没有则为null
                System.out.println("cookie");
                cart = cookieCart == null ? null : JsonUtils.jsonToList(cookieCart,CartItem.class);
            }
            model.addAttribute("cartList",cart);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return "cart";
    }

    /**
     * 添加商品到购物车
     * @param id  添加商品的id
     * @param amount 添加商品的数量
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/cart/add/{id}/{amount}")
    public String showPage(@PathVariable Long id,@PathVariable int amount,HttpServletRequest request, HttpServletResponse response,Model model){
        //获取cookie
        String cartListJson,tokenJson;
        tokenJson = CookieUtils.getCookieValue(request,"userToken");
        cartListJson = CookieUtils.getCookieValue(request,"cartList");
        RequestResult result = null;
        if(tokenJson != null) {
            result = userService.checkToken(tokenJson.split(":")[0]);
        }
        if(result != null && result.getStatus() == 200){
            System.out.println("已登录：");
            //已登录
            TbUser user = (TbUser)result.getData();
            Long uId = user.getId();
            //判断cookie里是否有购物车
            if(cartListJson != null && !"".equals(cartListJson)){
                //cookie中有购物车 进行合并
                ArrayList<CartItem> cookiesCart = (ArrayList<CartItem>)JsonUtils.jsonToList(cartListJson,CartItem.class);
                RequestResult result1 = cartService.addItemToCart(id,amount,uId,cookiesCart);
                System.out.println(result1.getData().toString());
                if(result1.getStatus() == 201){
                    CookieUtils.deleteCookie(request,response,"cartList");
                    System.out.println("201:清空cookie购物车");
                }
            }else{
                cartService.addItemToCart(id,amount,uId,null);
            }
        }else{
            System.out.println("未登录：");
            //未登录
            TbItem item = itemService.getItemById(id);
            CartItem cartItem = new CartItem();
            cartItem.setNum(amount);
            cartItem.setPrice(item.getPrice());
            cartItem.setTitle(item.getTitle());
            cartItem.setId(id);
            cartItem.setImage(item.getImage());
            ArrayList<CartItem> cartList = null;
            //判断是否存在购物车 不存在新建 存在将json转成对象
            if(cartListJson != null && !"".equals(cartListJson)){
                cartList = (ArrayList<CartItem>)JsonUtils.jsonToList(cartListJson,CartItem.class);
            }else{
                cartList = new ArrayList<CartItem>();
            }
            if(cartList.size() != 0) {
                CartItem rmCi = null;
                for (CartItem ci : cartList) {
                    if(ci.getId() == id){
                        //存在 将添加商品的数量修改诶诶添加后的商品
                        cartItem.setNum(ci.getNum()+amount);
                        //标记原来购物车的商品 在循环之后删除
                        rmCi = ci;
                        break;
                    }
                }
                if(rmCi != null){
                    //删除原先在购物车中数量为修改的商品
                    cartList.remove(rmCi);
                }
                //添加修改完数量之后的商品到购物车
                cartList.add(cartItem);
                }else{
                //cookie为空 购物车为新建的
                cartList.add(cartItem);
            }
            //把购物车写到客户端
            CookieUtils.setCookie(request,response,"cartList",JsonUtils.objectToJson(cartList));
            //展示购物车
            model.addAttribute("cartList",cartList);
        }
        return "redirect:/cart";
    }

    @RequestMapping("/cart/delete/{id}")
    public String deleteItem(Model model, @PathVariable Long id,HttpServletRequest request,HttpServletResponse response){
        String tokenJson = CookieUtils.getCookieValue(request,"userToken");
        //校验token
        RequestResult result = userService.checkToken(tokenJson!=null?tokenJson.split(":")[0]:"");
        List<CartItem> cart = new ArrayList<CartItem>();
        if(result.getStatus() == 200){
            //已登录
            TbUser user = (TbUser)result.getData();
            RequestResult serviceResult = cartService.delItemFromCart(user.getId(),id);
            cart = (List<CartItem>)serviceResult.getData();
        }else {
            //未登录 删cookie
            System.out.println("weidl");
            String cookieJson = CookieUtils.getCookieValue(request, "cartList");
            cart = JsonUtils.jsonToList(cookieJson, CartItem.class);
            for (int i = 0; i < cart.size(); i++) {
                CartItem ci = cart.get(i);
                if (ci.getId().equals(id)) {
                    cart.remove(ci);
                }
            }
        }
        CookieUtils.setCookie(request,response,"cartList",JsonUtils.objectToJson(cart));
        model.addAttribute("cartList",cart);
        return "redirect:/cart";
    }
}
