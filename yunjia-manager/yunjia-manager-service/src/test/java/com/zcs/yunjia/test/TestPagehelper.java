package com.zcs.yunjia.test;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zcs.yunjia.mapper.TbItemMapper;
import com.zcs.yunjia.pojo.TbItem;
import com.zcs.yunjia.pojo.TbItemExample;

public class TestPagehelper {
	
	@Test
	public void test(){
		//初始化spring容器
		ApplicationContext app1 = 
				new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
		//获取代理对象
		TbItemMapper itemMapper = app1.getBean(TbItemMapper.class);
		
		//设置分页信息 --使用pagehelper插件 页码  每页20条数据
		//只对紧跟在后面的一个查询有效
		PageHelper.startPage(1, 20);
		
		//查询条件  无参表示 select * from tb
		TbItemExample example = new TbItemExample();
		
		List<TbItem> items = itemMapper.selectByExample(example);
		
		//获取分页信息
		PageInfo<TbItem> f = new PageInfo<>(items);
		System.out.println("总数"+f.getTotal()+"---------"+f.getSize());
		
		for(TbItem i : items){
			System.out.println(i.getTitle()+i.getId());
		}
	}
}
