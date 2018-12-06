package com.zcs.yunjia.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.zcs.yunjia.common.pojo.EasyUITreeNode;
import com.zcs.yunjia.common.pojo.RequestResult;
import com.zcs.yunjia.content.service.CategoryService;
import com.zcs.yunjia.content.service.jedis.JedisClient;
import com.zcs.yunjia.mapper.TbContentCategoryMapper;
import com.zcs.yunjia.pojo.TbContentCategory;
import com.zcs.yunjia.pojo.TbContentCategoryExample;
import com.zcs.yunjia.pojo.TbContentCategoryExample.Criteria;


/**
 * 分类管理实现类
 *
 */
@Service
public class CategoryServiceImpl implements CategoryService {

	//注入dao
	@Autowired
	private TbContentCategoryMapper tbContentCategoryMapper;
	
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${PORTAL_SLIDE_KEY}")
	private String portal_slide_key;
	
	
	@Override
	/** 查询传来节点的所有子节点
	 * @param id 父节点id
	 * @return Easyui 异步树节点集合 
	 */
	public List<EasyUITreeNode> getCategory(Long id) {
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(id);
		List<TbContentCategory> list = tbContentCategoryMapper.selectByExample(example);
		return toEasyUItreeNode(list);
	}

	//增加节点/分类
	@Override
	/**
	 * parentId 父节点id
	 * name 前端传来的文本
	 */
	public RequestResult createCategory(Long parentId, String name) {
		
		TbContentCategory contentCategory = new TbContentCategory();
		//补全其他数据
		contentCategory.setParentId(parentId);
		contentCategory.setName(name);
		//在叶子节点上新建分类  需要将该节点设置为父节点 
		//判断该节点是否为叶子节点 是则将该节点修改为父节点 
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria createCriteria = example.createCriteria();
		createCriteria.andIdEqualTo(parentId);
		List<TbContentCategory> cs = tbContentCategoryMapper.selectByExample(example);
		if(cs.get(0).getIsParent() == false){
			//将原先的叶子节点改为父节点
			TbContentCategory parent = new TbContentCategory();
			parent.setId(parentId);
			parent.setIsParent(true);
			tbContentCategoryMapper.updateByPrimaryKeySelective(parent);
		}
		contentCategory.setIsParent(false);
		contentCategory.setIsParent(false);
		contentCategory.setCreated(new Date());
		contentCategory.setUpdated(new Date());
		contentCategory.setSortOrder(1);
		contentCategory.setStatus(1);
		//执行插入
		tbContentCategoryMapper.insert(contentCategory);
		//同步缓存
		syncRedis(portal_slide_key);
		
		//封装返回数据
		RequestResult result = new RequestResult();
		result.setData(contentCategory);
		result.setStatus(200);
		
		return result;
	}

	/**
	 * 分类重命名方法
	 * @param id 修改节点id
	 * @param newName 修改节点新名称
	 */
	@Override
	public List<EasyUITreeNode> updateCategoryName(Long id, String newName) {
		//设置新数据
		TbContentCategory tbContentCategory = new TbContentCategory();
		tbContentCategory.setId(id);
		tbContentCategory.setName(newName);
		tbContentCategory.setUpdated(new Date());
		
		
		/*TbContentCategoryExample example = new TbContentCategoryExample();
		//设置条件
		Criteria createCriteria = example.createCriteria();
		createCriteria.and.andIdEqualTo(id); //select * from tb where id=id
		//执行查询
		List<TbContentCategory> list = tbContentCategoryMapper.selectByExample(example);*/
		
		tbContentCategoryMapper.updateByPrimaryKeySelective(tbContentCategory);
		//同步缓存
		syncRedis(portal_slide_key);
		TbContentCategoryExample example = new TbContentCategoryExample();
		List<TbContentCategory> list = tbContentCategoryMapper.selectByExample(example);
		return toEasyUItreeNode(list);
	}
	
	@Override
	/**
	 * 删除分类节点 不允许删除父节点
	 */
	public RequestResult deleteCategory(Long id) {
		//查询是否为父节点
		TbContentCategory node = tbContentCategoryMapper.selectByPrimaryKey(id);
		RequestResult result = new RequestResult();
		if(node.getIsParent()){
			result.setStatus(444);
		}else{
			//查询要删除节点是否有兄弟节点 有则将父节点的isParent改为false
			Long parentId = node.getParentId();//查询该节点的父节点id
			//设置条件 查询兄弟节点
			TbContentCategoryExample example = new TbContentCategoryExample();
			Criteria criteria = example.createCriteria();
			criteria.andParentIdEqualTo(parentId);
			List<TbContentCategory> bros = tbContentCategoryMapper.selectByExample(example);
			if(bros.size()==1){//没有兄弟节点
				//将父节点的isParent设置为false
				TbContentCategory parent = new TbContentCategory();
				parent.setId(parentId);
				parent.setIsParent(false);
				tbContentCategoryMapper.updateByPrimaryKeySelective(parent);
				//同步缓存
				syncRedis(portal_slide_key);
				result.setData(tbContentCategoryMapper.selectByPrimaryKey(parentId));
			}
			//删除节点
			tbContentCategoryMapper.deleteByPrimaryKey(id);
			result.setStatus(200);
		}
		return result;
	}
	
	/**
	 * 将分类集合转换成  Easyui异步树节点集合
	 * @param list 分类集合
	 * @return Easyui异步树节点集合
	 */
	public List<EasyUITreeNode> toEasyUItreeNode(List<TbContentCategory> list){
		List<EasyUITreeNode> result = new ArrayList<EasyUITreeNode>();
		for(TbContentCategory e : list){
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(e.getId());
			node.setText(e.getName());
			node.setState(e.getIsParent()?"closed":"open");
			result.add(node);
		}
		return result;
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
