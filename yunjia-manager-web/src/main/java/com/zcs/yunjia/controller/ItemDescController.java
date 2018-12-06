package com.zcs.yunjia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zcs.yunjia.common.pojo.RequestResult;
import com.zcs.yunjia.service.ItemDescService;


/**
 * 商品描述Controller
 * @author zcs
 */
@Controller
public class ItemDescController {
	
	//注入service
	@Autowired
	private ItemDescService itemDescService;
	
	/**
	 * 查询商品描述
	 */
	@RequestMapping(value="/rest/item/query/item/desc/{itemId}")
	@ResponseBody
	public RequestResult getItemDesc(@PathVariable Long itemId){
		return itemDescService.getItemDesc(itemId);
	}
}
