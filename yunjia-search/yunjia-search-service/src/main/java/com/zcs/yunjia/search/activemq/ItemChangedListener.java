package com.zcs.yunjia.search.activemq;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;

import com.zcs.yunjia.common.pojo.SearchItem;
import com.zcs.yunjia.common.utils.SearchItemUtil;
import com.zcs.yunjia.search.mapper.SearchItemMapper;

/**
 * 商品修改监听器
 * @author zcs
 */
public class ItemChangedListener implements MessageListener {

	@Autowired
	private SearchItemMapper searchItemMapper;
	@Autowired
	private SolrServer solrServer;
	
	/**
	 * 监听器 
	 * 监听到商品修改消息后   通过传递过来的商品id 到数据库查询 并更新solr索引库
	 * 消息的格式为type：id
	 */
	public void onMessage(Message message) {
		if(message instanceof TextMessage){
			TextMessage mess = (TextMessage) message;
			try {
				//获取类型
				String type = mess.getText().split(":")[0];
				//获取商品id
				Long itemId = Long.parseLong(mess.getText().split(":")[1]);
				switch(type){
					case "insert" : case "update" : case "inStock" : case "outStock"  :{
						//查询
						SearchItem searchItem = searchItemMapper.getSearchItemById(itemId);
						//更新索引库
						try {
							solrServer.add(SearchItemUtil.SearchItemToSolrDoc(searchItem));
							solrServer.commit();
						} catch (SolrServerException | IOException e) {
							e.printStackTrace();
						}
					}
					break;
					case "delete" : {
						try {
							//删除索引
							solrServer.deleteByQuery("id:"+itemId);
							solrServer.commit();
						} catch (SolrServerException | IOException e) {
							e.printStackTrace();
						}
					}
					break;
				}
				System.out.println("search consumer:"+mess.getText());
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}

	}

}
