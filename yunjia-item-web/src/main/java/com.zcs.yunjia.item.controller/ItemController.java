package com.zcs.yunjia.item.controller;


import com.zcs.yunjia.item.pojo.Item;
import com.zcs.yunjia.item.service.ItemService;
import com.zcs.yunjia.pojo.TbItem;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
public class ItemController{

    @Resource
    private ItemService itemService;

    @RequestMapping("/item/{id}.html")
    public String getItemById(Model model,@PathVariable Long id){
        TbItem result = itemService.getItemById(id);
        Item item = tbItemToItem(result);
        item.setId(id);
        //将图片装换成数组注入
        item.setImages(result.getImage().split(","));
        model.addAttribute("item",item);
        return "item";
    }

    /**
     * 将Tbitem对面转换成Item对象 除了图片数组  Item对象里的图片为数组
     * @param tbItem
     * @return Item对象
     */
    private Item tbItemToItem(TbItem tbItem){
        Item item = new Item();
        item.setTitle(tbItem.getTitle());
        item.setBarcode(tbItem.getBarcode());
        item.setCid(tbItem.getCid());
        item.setCreated(tbItem.getCreated());
        item.setNum(tbItem.getNum());
        item.setPrice(tbItem.getPrice());
        item.setSellPoint(tbItem.getSellPoint());
        item.setStatus(tbItem.getStatus());
        item.setUpdated(tbItem.getUpdated());
        return item;
    }
}