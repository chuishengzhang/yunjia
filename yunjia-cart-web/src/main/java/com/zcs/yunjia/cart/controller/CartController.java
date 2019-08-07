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
        tokenJson = CookieUtils.getCookieValue(request,"userToken").split(":")[0];
        cartListJson = CookieUtils.getCookieValue(request,"cartList");
        RequestResult result = userService.checkToken(tokenJson);
        TbUser user = (TbUser)result.getData();
        Long uId = user.getId();
        if(result.getStatus() == 200){
            //已登录
            //判断cookie里是否有购物车
            if(cartListJson != null){
                //cookie中有购物车 进行合并
                ArrayList<CartItem> cookiesCart = (ArrayList<CartItem>)JsonUtils.jsonToList(cartListJson,CartItem.class);
                cartService.addItemToCart(id,amount,uId,cookiesCart);
            }
        }else{
            //未登录
            TbItem item = itemService.getItemById(id);
            CartItem cartItem = new CartItem();
            cartItem.setNum(amount);
            cartItem.setPrice(item.getPrice());
            cartItem.setTitle(item.getTitle());
            cartItem.setId(id);
            cartItem.setImage(item.getImage());
            tokenJson = CookieUtils.getCookieValue(request,"token");
            ArrayList<CartItem> cartList = null;
            //判断是否存在购物车 不存在新建 存在将json转成对象
            cartList = cartListJson == null ? new ArrayList<CartItem>() : (ArrayList<CartItem>)JsonUtils.jsonToList(cartListJson,CartItem.class);
            if(cartList.size() != 0) {
                for (int i=0 ;i <cartList.size(); i++) {
                    //判断是否已存在该商品
                    if(cartList.get(i).getId().equals(id)){
                        //存在 则合并数量
                        cartList.get(i).setNum(cartList.get(i).getNum()+amount);
                    }else{
                        cartList.add(cartItem);
                    }
                }
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

    @RequestMapping("/cart/add/{id}")
    public String addItem(Model model, @PathVariable Long id){
        TbItem item = itemService.getItemById(id);
        List<TbItem> items = new ArrayList<TbItem>();
        //数据库中的num为库存 此处设置为购买数量
        item.setNum(1);
        items.add(item);
        model.addAttribute("cartList",items);
        return "cart";
    }
}
