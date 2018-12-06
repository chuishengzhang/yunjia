package com.zcs.yunjia.content.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.zcs.yunjia.common.pojo.DataGridResult;
import com.zcs.yunjia.common.pojo.RequestResult;
import com.zcs.yunjia.common.utils.JsonUtils;
import com.zcs.yunjia.content.service.ContentService;
import com.zcs.yunjia.content.service.jedis.JedisClient;
import com.zcs.yunjia.mapper.TbContentMapper;
import com.zcs.yunjia.pojo.TbContent;
import com.zcs.yunjia.pojo.TbContentExample;
import com.zcs.yunjia.pojo.TbContentExample.Criteria;


@Service
public class ContentServiceImpl implements ContentService {
	
	//注入dao
	@Autowired
	private TbContentMapper tbContentMapper;
	@Value("${CLUSTER_HOST}")
	private String host;
	@Value("${CLUSTER_PORT}")
	private String ports;
	@Value("${PORTAL_SLIDE_KEY}")
	private String portal_slide_key;
	@Autowired
	private JedisClient jedisClient;
	

	/** 通过分类id 查询该分类下的所有内容 并分页
	 * @param categoryId 分类id
	 * @param page 第几页
	 * @param rows 每页行数
	 * @return 
	 */
	public DataGridResult getContentList(Long categoryId, Integer page, Integer rows) {
		//分页处理
		if(page == null | page ==0) page=1;
		if(rows == null | rows == 0) rows=20;
		PageHelper.startPage(page, rows);
		
		//查询分类内容列表
		List<TbContent> contentList = getAllContent(categoryId);
		
		//封装到DataGridResult
		DataGridResult data = new DataGridResult();
		data.setTotal(contentList.size());
		data.setRows(contentList);
		
		return data;
	}

	/**
	 * 新增分类内容
	 * @param tbContent 表单数据
	 * @return 
	 */
	public RequestResult saveContent(TbContent tbContent) {
		tbContent.setCreated(new Date());
		tbContent.setUpdated(new Date());
		//更新数据
		tbContentMapper.insertSelective(tbContent);
		//同步缓存
		syncRedis(portal_slide_key);
		
		RequestResult result = new RequestResult();
		result.setStatus(200);
		return result;
	}
	
	/**
	 * 查询分类id 下的所有内容
	 * @param categoryId 分类id
	 * @return 分类内容集合
	 */
	public List<TbContent> getAllContent(Long categoryId){
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		return tbContentMapper.selectByExample(example);
	}

	/**
	 * @param tbContent 修改内容表单数据
	 */
	public RequestResult editContent(TbContent tbContent) {
		//设置更新时间
		tbContent.setUpdated(new Date());
		int count = tbContentMapper.updateByPrimaryKeySelective(tbContent);
		//同步缓存
		syncRedis(portal_slide_key);
		RequestResult result = new RequestResult();
		int status = count==1?200:444;
		result.setStatus(status);
		return result;
	}

	/**
	 * 根据id删除分类内容
	 * @param id 删除分类内容的id
	 */
	public RequestResult deleteContents(Long[] ids) {
		//删除集合里的所有id
		int count = 0;
		for(Long id : ids){
			count += tbContentMapper.deleteByPrimaryKey(id);
		}
		//同步缓存
		syncRedis(portal_slide_key);
		RequestResult result = new RequestResult();
		int status = count >= 1 ? 200 : 444;
		result.setStatus(status);
		return result;
	}

	/**
	 * 查询该分类id下的所有内容
	 * 首页轮播图数据
	 */
	public List<TbContent> getContentAd(Long categoryId) {
		//判断是否存在redis缓存
		try{
			if(jedisClient.exists(portal_slide_key)){//有缓存
				System.out.println(portal_slide_key+"有缓存！！");
				//从缓存中取数据并返回
				return JsonUtils.jsonToList(jedisClient.hget(portal_slide_key, categoryId+""), TbContent.class);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		//查询
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		List<TbContent> contentList = tbContentMapper.selectByExample(example);
		
		//写入缓存
		try{
			jedisClient.hset(portal_slide_key, categoryId+"",JsonUtils.objectToJson(contentList));
			System.out.println(portal_slide_key+"没有缓存！！！");
		}catch(Exception e){
			e.printStackTrace();
		}
		return contentList;
	}
	
	/**
	 * 同步redis缓存 
	 * @param keys 删除的key
	 */
	public void syncRedis(String... keys){
		//同步redis
		try{
			for (String key : keys) {
				jedisClient.del(key);//删除key
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
