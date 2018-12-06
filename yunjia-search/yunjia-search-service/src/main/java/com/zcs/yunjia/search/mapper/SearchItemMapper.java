package com.zcs.yunjia.search.mapper;

import java.util.List;

import com.zcs.yunjia.common.pojo.SearchItem;


public interface SearchItemMapper{
	
	//获取查询数据列表
	public List<SearchItem> importSearchItemList();
	//根据id查询
	public SearchItem getSearchItemById(Long id);
	//根据id删除
	public int delSearchItemById(Long id);

}
