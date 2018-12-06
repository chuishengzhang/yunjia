package com.zcs.yunjia.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zcs.yunjia.common.pojo.EasyUITreeNode;
import com.zcs.yunjia.common.pojo.RequestResult;
import com.zcs.yunjia.content.service.CategoryService;


@Controller
public class CategoryController {
	
	@Autowired
	private CategoryService cateGoryService;
	
	//显示内容管理分类页面
	@ResponseBody
	@RequestMapping(value="/content/category/list",method=RequestMethod.GET)
	public List<EasyUITreeNode> showCategory(@RequestParam(value="id",defaultValue="0")Long id){
		return cateGoryService.getCategory(id);
	}
	
	//增加节点
	@RequestMapping(value="/content/category/create",method=RequestMethod.POST)
	@ResponseBody
	public RequestResult createContegory(Long parentId,String name){
		return cateGoryService.createCategory(parentId, name);
	}
	
	//重命名
	@RequestMapping(value="/content/category/update",method=RequestMethod.POST)
	@ResponseBody
	public List<EasyUITreeNode> updateCategoryName(Long id,String name){
		return cateGoryService.updateCategoryName(id, name);
	}
	
	/**
	 * 删除节点
	 * @param id 删除节点的id
	 */
	@RequestMapping(value="/content/category/delete/",method=RequestMethod.POST)
	@ResponseBody
	public RequestResult deleteCategory(Long id){
		return cateGoryService.deleteCategory(id);
	}
	
}
