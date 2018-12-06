package com.zcs.yunjia.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zcs.yunjia.common.pojo.DataGridResult;
import com.zcs.yunjia.common.pojo.RequestResult;
import com.zcs.yunjia.pojo.TbItem;
import com.zcs.yunjia.service.ItemService;


/**
 * 商品显示相关controller
 *
 */
@Controller
public class ItemController {
	
	//注入service
	@Autowired
	private ItemService itemService;
	
	//查询商品列表
	@RequestMapping(value="/item/list",method=RequestMethod.GET)
	@ResponseBody
	public DataGridResult getItemList(Integer page,Integer rows){
		DataGridResult data = itemService.itemList(page, rows);
		return data;
	}
	
	/**
	 * 添加商品
	 */
	@RequestMapping(value="/item/save",method=RequestMethod.POST)
	@ResponseBody
	public RequestResult saveItem(TbItem tbItem,String desc,String itemParams){
		return itemService.saveItem(tbItem, desc, itemParams);
	}
	
	/**
	 * 更新商品信息
	 * @param tbItem 前端表单数据
	 * @param desc 商品描述
	 */
	@RequestMapping(value="/rest/item/update",method=RequestMethod.POST)
	@ResponseBody
	public RequestResult updateItem(TbItem tbItem,String desc){
		return itemService.updateItem(tbItem, desc);
	}
	
	/**
	 * 删除商品
	 * @param ids 要删除的商品id数组
	 */
	@RequestMapping(value="/rest/item/delete",method=RequestMethod.POST)
	@ResponseBody
	public RequestResult deleteItems(Long[] ids){
		return itemService.deleteItems(ids);
	}
	
	/**
	 * 上架商品
	 * 商品status 1-表示正常   2-表示下架
	 * @param ids 要上架的商品的id
	 * @return status 200-成功  444-失败  333-该商品修改前已是上架状态
	 */
	@RequestMapping(value="/rest/item/instock",method=RequestMethod.POST)
	@ResponseBody
	public RequestResult inStock(Long[] ids){
		return itemService.inStock(ids);
	}
	
	/**
	 * 下架商品
	 * 商品status 1-表示正常   2-表示下架
	 * @param ids 要下架的商品的id
	 * @return status 200-成功  444-失败  333-该商品修改前已是下架状态
	 */
	@RequestMapping(value="/rest/item/outstock",method=RequestMethod.POST)
	@ResponseBody
	public RequestResult outStock(Long[] ids){
		return itemService.outStock(ids);
	}
}
