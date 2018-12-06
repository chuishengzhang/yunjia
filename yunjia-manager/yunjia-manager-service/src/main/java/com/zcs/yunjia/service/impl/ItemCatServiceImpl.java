package com.zcs.yunjia.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zcs.yunjia.common.pojo.EasyUITreeNode;
import com.zcs.yunjia.mapper.TbItemCatMapper;
import com.zcs.yunjia.pojo.TbItemCat;
import com.zcs.yunjia.pojo.TbItemCatExample;
import com.zcs.yunjia.pojo.TbItemCatExample.Criteria;
import com.zcs.yunjia.service.ItemCatService;


@Service
public class ItemCatServiceImpl implements ItemCatService {

	//注入dao
	@Autowired
	private TbItemCatMapper tbItemCatMapper;
	
	/**
	 * 商品类别列表
	 */
	public List<EasyUITreeNode> getItemCatList(Long parentId) {
		//查询所有
		TbItemCatExample example = new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbItemCat> itemCats = tbItemCatMapper.selectByExample(example);
		
		List<EasyUITreeNode> results = new ArrayList<EasyUITreeNode>();
		for(TbItemCat tic : itemCats){
			EasyUITreeNode result = new EasyUITreeNode();
			result.setId(tic.getId());
			result.setState(tic.getIsParent()?"closed":"open");
			result.setText(tic.getName());
			results.add(result);
		}
		return results;
	}

}
