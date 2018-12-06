package com.zcs.yunjia.service;

import java.util.List;

import com.zcs.yunjia.common.pojo.EasyUITreeNode;


/**
 * 商品列表接口
 *
 */
public interface ItemCatService {
	
	//查询商品类别列表
	public List<EasyUITreeNode> getItemCatList(Long parentId);
}
