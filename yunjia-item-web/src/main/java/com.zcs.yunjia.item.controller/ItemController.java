package com.zcs.yunjia.item.controller;


import com.zcs.yunjia.item.pojo.Item;
import com.zcs.yunjia.item.service.ItemService;
import com.zcs.yunjia.pojo.TbItem;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class ItemController{

    @Resource
    private ItemService itemService;

    @RequestMapping("/item/{id}.html")
    public String getItemById(Model model,@PathVariable Long id){
        TbItem item = itemService.getItemById(id);
        model.addAttribute("item",item);
        return "item";
    }
}