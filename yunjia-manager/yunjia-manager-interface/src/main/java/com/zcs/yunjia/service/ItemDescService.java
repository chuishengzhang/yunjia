package com.zcs.yunjia.service;

import com.zcs.yunjia.common.pojo.RequestResult;

/**
 * 商品描述接口
 * @author zcs
 *
 */
public interface ItemDescService {
	
	//加载商品描述
	public RequestResult getItemDesc(Long itemId);
}
