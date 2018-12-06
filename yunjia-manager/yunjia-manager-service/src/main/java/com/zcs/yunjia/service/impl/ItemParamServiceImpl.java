package com.zcs.yunjia.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zcs.yunjia.common.pojo.DataGridResult;
import com.zcs.yunjia.common.pojo.RequestResult;
import com.zcs.yunjia.mapper.TbItemParamMapper;
import com.zcs.yunjia.pojo.TbItemParam;
import com.zcs.yunjia.pojo.TbItemParamExample;
import com.zcs.yunjia.pojo.TbItemParamWithCatName;
import com.zcs.yunjia.pojo.TbItemParamWithCatNameExample;
import com.zcs.yunjia.pojo.TbItemParamExample.Criteria;
import com.zcs.yunjia.service.ItemParamService;

@Service
public class ItemParamServiceImpl implements ItemParamService {

	//注入dao
	@Autowired
	private TbItemParamMapper tbItemParamMapper;
	
	/**
	 * 查询商品规格列表 并进行分页处理
	 * @param page 第几页
	 * @param rows 每页显示的条数
	 */
	public DataGridResult getItemParamList(Integer page,Integer rows) {
		//分页处理
		if(page == null || page <=0) page=1;
		if(rows == null || rows<=0) rows=30;
		PageHelper.startPage(page, rows);
		
		//查询商品规格列表 select * from ..		
		List<TbItemParamWithCatName> itemParams = tbItemParamMapper.selectItemParamWithItemName(new TbItemParamWithCatNameExample());
		
		//封装到DataGrid
		DataGridResult result = new DataGridResult();
		
		result.setTotal((int)new PageInfo<TbItemParamWithCatName>(itemParams).getTotal());
		result.setRows(itemParams);
		return result;
	}

	/**
	 * 根据商品类别id查询 商品规格模板
	 */
	public RequestResult getItemParamByCatId(Long itemCatId) {
		//设置条件
		TbItemParamExample example = new TbItemParamExample();
		Criteria criteria = example.createCriteria();
		criteria.andItemCatIdEqualTo(itemCatId);
		
		RequestResult result = new RequestResult();
		//执行查询
		List<TbItemParam> itemParams = tbItemParamMapper.selectByExampleWithBLOBs(example);
		if(itemParams == null || itemParams.isEmpty()){
			result.setStatus(444);
			return result;
		}
		//集合有数据
		result.setStatus(200);
		result.setData(itemParams.get(0));
		
		return result;
	}

	/**
	 * 新增商品规格模板
	 * @param itemCatId 新增模板的分类id
	 * @param paramData 规格模板的格式  json的字符串
	 */
	public RequestResult saveItemParam(Long itemCatId, String paramData) {
		//设置数据
		TbItemParam itemParam = new TbItemParam();
		itemParam.setItemCatId(itemCatId);
		itemParam.setParamData(paramData);
		itemParam.setCreated(new Date());
		itemParam.setUpdated(new Date());
		//执行插入
		int insertRows = tbItemParamMapper.insert(itemParam);
		//返回结果
		RequestResult result= new RequestResult();
		int status = insertRows == 1 ? 200 : 444;
		result.setStatus(status);
		
		return result;
	}

	/**
	 *根据id删除模板
	 * @param ids 要删除模板的id集数组
	 */
	public RequestResult deleteItemParams(Long[] ids) {
		//执行删除
		int deleteRows = 0;
		for (Long id : ids) {
			deleteRows += tbItemParamMapper.deleteByPrimaryKey(id);
		}
		//返回结果
		RequestResult result = new RequestResult();
		int status = deleteRows == ids.length ? 200 : 444;
		result.setStatus(status);
		
		return result;
	}

	/**
	 * 根据模板id查询规格数据
	 * @param id 模板的id
	 */
	public RequestResult getItemParamById(Long id) {
		TbItemParam itemParam = tbItemParamMapper.selectByPrimaryKey(id);
		RequestResult result = new RequestResult();
		result.setStatus(200);
		result.setData(itemParam);
		return result;
	}

}
