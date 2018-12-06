package com.zcs.yunjia.content.service;

import java.util.List;

import com.zcs.yunjia.common.pojo.DataGridResult;
import com.zcs.yunjia.common.pojo.PortalCatResult;
import com.zcs.yunjia.common.pojo.RequestResult;
import com.zcs.yunjia.pojo.TbContent;


/**
 * 
 * 分类内容接口
 *
 */
public interface ContentService{
	//查询分类内容
	public DataGridResult getContentList(Long categoryId,Integer page,Integer rows);

	//新增分类内容
	public RequestResult saveContent(TbContent tbContent);
	
	//修改分类内容
	public RequestResult editContent(TbContent tbContent);
	
	//删除分类内容
	public RequestResult deleteContents(Long[] ids);
	
	//首页轮播图
	public List<TbContent> getContentAd(Long categoryId);
	
}
