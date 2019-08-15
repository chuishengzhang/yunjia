package com.zcs.yunjia.order.controller;

import com.zcs.yunjia.cart.pojo.CartItem;
import com.zcs.yunjia.cart.service.CartService;
import com.zcs.yunjia.pojo.TbUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private CartService cartService;

    @RequestMapping(value = {"/","/order"})
    public String showPage(HttpServletRequest request, Model model){
        //从request中获取拦截器写入的用户数据
        TbUser user = (TbUser)request.getAttribute("user");
        //从redis取购物车
        List<CartItem> userCart = cartService.getUserCart(user.getId());
        //图片处理
        for (CartItem ci: userCart) {
            if(ci.getImage().contains(",")){
                ci.setImage(ci.getImage().split(",")[0]);
            }
        }
        model.addAttribute("cartList",userCart);
        return "order-cart";
    }
}
