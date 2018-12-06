package com.zcs.yunjia.common.utils;

import java.io.Serializable;

import org.apache.solr.common.SolrInputDocument;

import com.zcs.yunjia.common.pojo.SearchItem;

/**
 * 搜索商品相关操作工具类
 * @author zcs
 */
public class SearchItemUtil implements Serializable{
	
	/**
	 * 将SearchItem转换成solrDocument
	 * @param item 搜索商品信息
	 * @return solrDocument
	 */
	public static SolrInputDocument SearchItemToSolrDoc(SearchItem item){
		SolrInputDocument doc = new SolrInputDocument();
		doc.addField("id", item.getId());
		doc.addField("item_title", item.getTitle());
		doc.addField("item_sell_point", item.getSell_point());
		doc.addField("item_pirce", item.getPrice());
		doc.addField("item_image", item.getImage());
		doc.addField("item_cat_name", item.getItemCatName());
		doc.addField("item_desc", item.getItem_desc());
		return doc;
	}
}
