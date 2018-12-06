package com.zcs.yunjia.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zcs.yunjia.common.pojo.RequestResult;
import com.zcs.yunjia.mapper.TbItemDescMapper;
import com.zcs.yunjia.pojo.TbItemDesc;
import com.zcs.yunjia.service.ItemDescService;


@Service
public class ItemDescServiceImpl implements ItemDescService {

	//注入dao
	@Autowired
	private TbItemDescMapper itemDescMapper;
	
	/**
	 * 查询商品描述
	 * @param itemId 查询商品的id
	 */
	public RequestResult getItemDesc(Long itemId) {
		//查询商品
		TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
		
		//封装到RequestResult
		RequestResult result = new RequestResult();
		int status = itemDesc == null ? 444 : 200;
		System.out.println(itemId+"hahah");
		//System.out.println(itemId+"miaoshu"+itemDesc.getItemDesc());
		result.setData(itemDesc);
		result.setStatus(status);
		
		return result;
	}

}
