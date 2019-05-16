package com.zcs.yunjia.item.controller;


import com.zcs.yunjia.item.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ItemController{

    @Autowired
    private ItemService itemService;

    @RequestMapping("item/{id},html")
    @ResponseBody
    public void getItemById(@PathVariable Long id){
        itemService.getItemById(id);
    }
}