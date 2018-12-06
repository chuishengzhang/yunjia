package com.zcs.yunjia.service;

import com.zcs.yunjia.common.pojo.DataGridResult;
import com.zcs.yunjia.common.pojo.RequestResult;

/**
 * 商品规格参数接口
 * @author zcs
 *
 */
public interface ItemParamService {
	
	//查询商品规格列表
	public DataGridResult getItemParamList(Integer page,Integer rows);
	
	//根据分类id查询规格模板
	public RequestResult getItemParamByCatId(Long itemCatId);
	
	//新增模板
	public RequestResult saveItemParam(Long itemCatId,String paramData);
	
	//删除模板
	public RequestResult deleteItemParams(Long[] ids);
	
	//根据规格模板id查询规格
	public RequestResult getItemParamById(Long id);
}
