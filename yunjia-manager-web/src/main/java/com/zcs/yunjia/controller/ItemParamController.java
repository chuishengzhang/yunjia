package com.zcs.yunjia.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zcs.yunjia.common.pojo.DataGridResult;
import com.zcs.yunjia.common.pojo.RequestResult;
import com.zcs.yunjia.service.ItemParamService;

@Controller
public class ItemParamController {
	
	//注入service
	@Autowired
	private ItemParamService itemParamService;
	
	/**
	 * 查询商品规格列表
	 * @param page 第几页
	 * @param rows 每页的个数
	 */
	@RequestMapping(value="/item/param/list",method=RequestMethod.GET)
	@ResponseBody
	public DataGridResult getItemParamList(Integer page,Integer rows){
		return itemParamService.getItemParamList(page,rows);
	}
	
	/**
	 * 根据商品分类id 查询该商品是否有规格模板
	 * @param itemCatId 商品类别id
	 */
	@RequestMapping("/item/param/query/itemcatid/{id}")
	@ResponseBody
	public RequestResult getItemParam(@PathVariable Long id){
		return itemParamService.getItemParamByCatId(id);
	}
	
	/**
	 * 新增商品规格模板
	 * @param itemCatId 新增模板的分类id
	 * @param paramData 新增规格模板的格式  json格式的字符串
	 */
	@RequestMapping(value="/item/param/save/{cid}",method=RequestMethod.POST)
	@ResponseBody
	public RequestResult saveItemParam(@PathVariable(value="cid") Long itemCatId,String paramData){
		return itemParamService.saveItemParam(itemCatId, paramData);
	}
	
	/**
	 * 根据id删除规格模板
	 * @param ids 要删除的规格模板的id数组
	 */
	@RequestMapping(value="/item/param/delete",method=RequestMethod.POST)
	@ResponseBody
	public RequestResult deleteItemParams(Long[] ids){
		return itemParamService.deleteItemParams(ids);
	}
	
	/**
	 * 根据分类id查询规格模板
	 */
	@RequestMapping("/item/param/edit")
	@ResponseBody
	public RequestResult itemParamEdit(Long id){
		return itemParamService.getItemParamById(id);
	}
	
	/**
	 *  显示模板修改页面  并显示数据
	 *  根据模板id查询 模板数据 并
	 * @param itemParamId 模板id
	 * @param 模型 封装查询到的规格模板信息
	 *//*
	@RequestMapping("/item/param/edithaha")
	public String getEditItemParam(Model model){
		//执行查询
		RequestResult result = itemParamService.getItemParamById(2l);
		TbItemParam itemParam = (TbItemParam) result.getData();
		//获取规格模板信息
		String jsonData = itemParam.getParamData();
		//将json转为对象
		List<Map> objData = JsonUtils.jsonToList(jsonData, Map.class);
		StringBuffer sb = new StringBuffer();
		sb.append("<table border=0>");
		for(Map map : objData){
			String title = (String) map.get("group");
			List<String> a = (List<String>) map.get("params");
			sb.append("	<tr>");
			sb.append("		<th rowspan="+a.size()+"><textarea style='width:100%;height:100%' value='"+title+"'></textarea></th>");
			for (String item : a) {
				sb.append("		<td><input type='text' value='"+item+"' /></td>");
				sb.append("	</tr>");
			}
		}
		sb.append("</table>");
		System.out.println(sb);
		model.addAttribute("htm", sb);
		return "item-param-edit";
	}*/
}
