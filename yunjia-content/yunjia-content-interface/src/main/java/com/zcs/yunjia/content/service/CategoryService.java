package com.zcs.yunjia.content.service;


import java.util.List;

import com.zcs.yunjia.common.pojo.EasyUITreeNode;
import com.zcs.yunjia.common.pojo.PortalCatResult;
import com.zcs.yunjia.common.pojo.RequestResult;
import com.zcs.yunjia.pojo.TbContentCategory;


/**
 * 分类管理接口
 *
 */
public interface CategoryService {
	
	//分类管理列表
	public List<EasyUITreeNode> getCategory(Long id);
	
	//增加分类
	public RequestResult createCategory(Long parenntId,String name);
	
	//分类重命名
	public List<EasyUITreeNode> updateCategoryName(Long id,String newName);
	
	//删除分类
	public RequestResult deleteCategory(Long id);
	
	
}
