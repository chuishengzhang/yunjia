package com.zcs.yunjia.search.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zcs.yunjia.common.pojo.RequestResult;
import com.zcs.yunjia.common.pojo.SearchItem;
import com.zcs.yunjia.common.pojo.SearchResult;
import com.zcs.yunjia.search.service.SearchService;

@Controller
public class SearchController {
	
	@Autowired
	private SearchService searchService;
	@Value("${SEARCH_ROWS}")
	private String SEARCH_ROWS;
	
	//导入solr索引库
	@RequestMapping("/solr/import")
	@ResponseBody
	public RequestResult importSearchItemList() {
		return searchService.importSearchItemList();
	}
	
	@RequestMapping("/search")
	public String showSearch(Model model,@RequestParam("q") String q,@RequestParam(defaultValue="0") Integer page) throws  Exception{
		String queryStr = new String(q.getBytes("iso-8859-1"), "utf-8");
		SearchResult result = searchService.getSearchResult(queryStr, page, Integer.valueOf(SEARCH_ROWS));
		List<SearchItem> itemList = result.getItemList();//获取搜索结果集合
		model.addAttribute("itemList",itemList);//搜索结果
		model.addAttribute("totalPages", result.getTotalPages());//总页数
		model.addAttribute("page",page);//第几页
		model.addAttribute("query",queryStr);//查询字段
		return "search";
	}
	
}
