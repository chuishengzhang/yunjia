package com.zcs.yunjia.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zcs.yunjia.common.pojo.EasyUITreeNode;
import com.zcs.yunjia.service.ItemCatService;


@Controller
public class ItemCatController {
	
	//注入service
	@Autowired
	private ItemCatService itemCatService;
	
	/**
	 * 显示添加商品类别  商品添加页面的类别选择
	 */
	@RequestMapping(value="/item/cat/list",method=RequestMethod.POST)
	@ResponseBody
	public List<EasyUITreeNode> getItemCatList(@RequestParam(value="id",defaultValue="0")Long parentId){
		return itemCatService.getItemCatList(parentId);
	}
}
