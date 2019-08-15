package com.zcs.yunjia.order.controller;

import com.zcs.yunjia.cart.pojo.CartItem;
import com.zcs.yunjia.cart.service.CartService;
import com.zcs.yunjia.common.pojo.OrderInfo;
import com.zcs.yunjia.common.pojo.RequestResult;
import com.zcs.yunjia.order.service.OrderService;
import com.zcs.yunjia.pojo.TbOrder;
import com.zcs.yunjia.pojo.TbUser;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private CartService cartService;
    @Autowired
    private OrderService orderService;

    @RequestMapping(value = {"/","/order"})
    public String showPage(HttpServletRequest request, Model model){
        //从request中获取拦截器写入的用户数据
        TbUser user = (TbUser)request.getAttribute("user");
        //从redis取购物车
        List<CartItem> userCart = cartService.getUserCart(user.getId());
        //图片处理
        for (CartItem ci: userCart) {
            //图片可能有多张用逗号分隔，这里只取第一张图片展示
            String images = ci.getImage();
            if(images.contains(",")){
                ci.setImage(images.split(",")[0]);
            }
        }
        model.addAttribute("cartList",userCart);
        return "order-cart";
    }

    @RequestMapping("/order/create")
    public String createOrder(OrderInfo orderInfo,Model model){
        RequestResult result = orderService.createOrder(orderInfo);
        model.addAttribute("orderId",result.getData());
        model.addAttribute("payment",orderInfo.getPayment());
        DateTime dateTime = new DateTime();
        model.addAttribute("date",dateTime.plusDays(3).toString("yyyy-MM-dd"));
        return "success";
    }
}
