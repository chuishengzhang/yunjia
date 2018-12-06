package com.zcs.yunjia.common.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 搜索返回结果的pojo
 * @author zcs
 */
public class SearchResult implements Serializable {
	private List<SearchItem> itemList;//搜索结果集合
	private Integer totalPages;//总页数
	public List<SearchItem> getItemList() {
		return itemList;
	}
	public void setItemList(List<SearchItem> itemList) {
		this.itemList = itemList;
	}
	public Integer getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}
	
}
