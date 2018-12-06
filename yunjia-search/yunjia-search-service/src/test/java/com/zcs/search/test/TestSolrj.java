package com.zcs.search.test;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import com.zcs.yunjia.common.pojo.SearchResult;

public class TestSolrj {
	@Test
	public void testSolrjs() throws Exception{
		//1.创建Solr服务
		SolrServer solrServer = new HttpSolrServer("http://47.100.214.167:8983/solr/yunjia");
		//2.创建文档
		SolrInputDocument doc = new SolrInputDocument();
		//3.向文档添加数据
		doc.addField("id", "testId");//id是必须的
		doc.addField("item_title", "solrj测试");
		//4.添加文档到server
		solrServer.add(doc);
		//5.提交到索引库
		solrServer.commit();
	}
	
	@Test
	public void testSolrjQuery() throws Exception{
		//1.创建Solr服务
		SolrServer solrServer = new HttpSolrServer("http://47.100.214.167:8983/solr/yunjia");
		//创建查询
		SolrQuery query = new SolrQuery();
		//设置查询条件
		query.setQuery("手机");
		query.set("df", "item_keywords");
		//执行查询
		QueryResponse queryResponse = solrServer.query(query);
		//获取查询结果
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		for (SolrDocument solrDocument : solrDocumentList) {
			System.out.println(solrDocument.get("item_title"));
		}
		
	}
	@Test
	public void testSearch() throws Exception{
		System.out.println("start");
		SolrServer solrServer = new HttpSolrServer("http://47.100.214.167:8983/solr/yunjia");
		//创建查询
		SolrQuery query = new SolrQuery();
		//设置条件
		query.setQuery("*:*");
		//设置默认域
		query.set("df", "item_keywords");
		query.setStart(0);
		query.setRows(10000);
		//执行查询
		QueryResponse response = solrServer.query(query);
		SolrDocumentList results = response.getResults();
		for (SolrDocument s : results) {
			System.out.println(s.getFieldValue("item_title").toString());
		}
		System.out.println("end");
	}
}
