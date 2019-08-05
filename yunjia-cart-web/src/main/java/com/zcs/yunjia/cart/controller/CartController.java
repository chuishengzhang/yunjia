package com.zcs.yunjia.cart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.xml.ws.RequestWrapper;

@Controller
public class CartController {

    @RequestMapping("/gg")
    public String showPage(){
        return "cart";
    }
}
