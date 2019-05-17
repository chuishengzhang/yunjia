package com.zcs.yunjia.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zcs.yunjia.common.pojo.DataGridResult;
import com.zcs.yunjia.common.pojo.RequestResult;
import com.zcs.yunjia.common.utils.IDUtils;
import com.zcs.yunjia.mapper.TbItemDescMapper;
import com.zcs.yunjia.mapper.TbItemMapper;
import com.zcs.yunjia.mapper.TbItemParamMapper;
import com.zcs.yunjia.pojo.TbItem;
import com.zcs.yunjia.pojo.TbItemDesc;
import com.zcs.yunjia.pojo.TbItemExample;
import com.zcs.yunjia.pojo.TbItemParam;
import com.zcs.yunjia.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {
	
	//注入dao
	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper itemDescMapper;
	@Autowired
	private TbItemParamMapper tbItemParamMapper;
	@Autowired
	private JmsTemplate jmsTemplate; //jms模板
	@Resource
	private Destination destination; //目的地

	@Override
	//商品列表
	public DataGridResult itemList(Integer page,Integer rows) {
		
		if(page == null | page ==0) page=1;
		if(rows == null | rows == 0) rows=20;
		//设置分页信息
		PageHelper.startPage(page, rows);
		
		//查询
		TbItemExample example = new TbItemExample();
		List<TbItem> items = itemMapper.selectByExample(example);
		
		//封装到DatagridResult中
		DataGridResult data = new DataGridResult();
		data.setRows(items);
		PageInfo<TbItem> pageInfo = new PageInfo<>(items);
		data.setTotal((int)pageInfo.getTotal());
		
		return data;
	}

	/**
	 * 添加商品
	 * @param newItem 前端表单数据
	 * @param desc 商品描述
	 * @param params 商品规格json
	 * @return result status=200:成功 444：失败  999：cid为空
	 */
	public RequestResult saveItem(TbItem tbItem,String desc,String params){
		RequestResult result = new RequestResult();
		if(tbItem.getCid()== null){
			result.setStatus(999);
			return result;
		}
		
		//补全商品信息
		tbItem.setId(IDUtils.genItemId());
		tbItem.setImage(tbItem.getImage());
		System.out.println("save Item image url:"+tbItem.getImage());
		tbItem.setStatus(new Byte("1"));
		tbItem.setCreated(new Date());
		tbItem.setUpdated(new Date());
		
		//插入商品  返回受影响的行数
		int rows = itemMapper.insert(tbItem);
		
		//新增商品描述
		TbItemDesc itemDesc = new TbItemDesc();
		itemDesc.setItemId(tbItem.getId());
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(tbItem.getCreated());
		itemDesc.setUpdated(tbItem.getUpdated());
		itemDescMapper.insert(itemDesc);
		
		//插入商品规格
		TbItemParam itemParam = new TbItemParam();
		itemParam.setItemCatId(tbItem.getCid());
		itemParam.setParamData(params);
		itemParam.setCreated(new Date());
		itemParam.setUpdated(new Date());
		tbItemParamMapper.insert(itemParam);
		
		
		//插入成功状态
		int status = rows>0 ? 200 : 444;
		result.setStatus(status);
		
		//发送消息
		sendTopic(tbItem.getId(),"insert");
		
		return result;
	}

	/**
	 * 更新商品信息
	 * @param tbItem 前端表单数据
	 * @param desc 修改的商品描述
	 */
	public RequestResult updateItem(TbItem tbItem, String desc) {
		//添加修改时间
		tbItem.setUpdated(new Date());
		//修改商品 返回受影响行数
		int itemRows = itemMapper.updateByPrimaryKeySelective(tbItem);
		
		//修改商品描述
		TbItemDesc itemDesc = new TbItemDesc();
		itemDesc.setItemId(tbItem.getId());
		itemDesc.setItemDesc(desc);
		itemDesc.setUpdated(new Date());
		int itemDescRows = itemDescMapper.updateByPrimaryKeySelective(itemDesc);
		
		//封装返回结果
		int status = itemRows+itemDescRows == 2 ? 200 : 444;
		RequestResult result = new RequestResult();
		result.setStatus(status);
		
		//发送消息
		sendTopic(tbItem.getId(), "update");
		
		return result;
	}

	/**
	 * 删除商品
	 * @param ids 要删除商品的id数组
	 */
	public RequestResult deleteItems(Long[] ids) {
		int deleteRows = 0;
		int descRows = 0;
		for(Long id : ids){
			//删除商品
			deleteRows += itemMapper.deleteByPrimaryKey(id);
			//删除商品描述
			descRows += itemDescMapper.deleteByPrimaryKey(id);
			//发送消息
			sendTopic(id, "delete");
		}
		//封装返回结果
		RequestResult result= new RequestResult();
		int status = deleteRows+descRows == ids.length*2 ? 200 : 444;
		result.setStatus(status);
		return result;
	}

	/**
	 * 上架商品
	 * 商品status 1-表示正常   2-表示下架
	 * @param ids 要上架的商品的id
	 * @return status 200-成功  444-失败  333-该商品修改前已是上架状态
	 */
	public RequestResult inStock(Long[] ids) {
		int updateRows = 0;
		RequestResult result = new RequestResult();
		for(Long id : ids){
			//判断商品状态
			TbItem item = itemMapper.selectByPrimaryKey(id);
			if(item.getStatus() == 1){
				result.setStatus(333);
				return result;
			}else{
				TbItem newItem = new TbItem();
				newItem.setId(id);
				newItem.setStatus((byte)1);
				updateRows += itemMapper.updateByPrimaryKeySelective(newItem);
				//发送消息
				sendTopic(newItem.getId(), "instock");
			}
		}
		int status = updateRows == ids.length ? 200 : 444;
		result.setStatus(status);
		return result;
	}

	/**
	 * 下架商品
	 * 商品status 1-表示正常   2-表示下架
	 * @param ids 要下架的商品的id
	 * @return status 200-成功  444-失败  333-该商品修改前已是下架状态
	 */
	public RequestResult outStock(Long[] ids) {
		int updateRows = 0;
		RequestResult result = new RequestResult();
		for(Long id : ids){
			//判断商品状态
			TbItem item = itemMapper.selectByPrimaryKey(id);
			if(item.getStatus() == 2){
				result.setStatus(333);
				return result;
			}else{
				TbItem newItem = new TbItem();
				newItem.setId(id);
				newItem.setStatus((byte)2);
				updateRows += itemMapper.updateByPrimaryKeySelective(newItem);
				//发送消息
				sendTopic(newItem.getId(), "outStock");
			}
		}
		int status = updateRows == ids.length ? 200 : 444;
		result.setStatus(status);
		return result;
	}

	/**
	 * 商品数据变更后发送消息 通知其他系统
	 * @param itemId 改变商品id
	 * @param type 商品改变类型
	 */
	public void sendTopic(Long itemId,String type){
		jmsTemplate.send(destination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				System.out.println("send:"+type+":"+itemId);
				return session.createTextMessage(type+":"+itemId);
			}
		});
	}
}
