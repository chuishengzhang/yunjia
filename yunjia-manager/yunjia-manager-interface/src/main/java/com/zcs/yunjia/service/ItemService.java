package com.zcs.yunjia.service;


import com.zcs.yunjia.common.pojo.DataGridResult;
import com.zcs.yunjia.common.pojo.RequestResult;
import com.zcs.yunjia.pojo.TbItem;

/**
 * 商品相关接口
 * @author 1
 *
 */
public interface ItemService {
	
	//查询item列表
	public DataGridResult itemList(Integer page,Integer rows);
	
	//添加商品
	public RequestResult saveItem(TbItem tbItem,String desc,String params);
	
	//更新商品
	public RequestResult updateItem(TbItem tbItem,String desc);
	
	//删除商品
	public RequestResult deleteItems(Long[] ids);
	
	//上架商品
	public RequestResult inStock(Long[] ids);
	
	//下架商品
	public RequestResult outStock(Long[] ids);
	
	
}
