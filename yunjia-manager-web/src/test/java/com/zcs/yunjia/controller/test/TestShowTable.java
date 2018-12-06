package com.zcs.yunjia.controller.test;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.zcs.yunjia.common.utils.JsonUtils;


public class TestShowTable {
	
	@Test
	public void showTable(){
		String paramData = "[{\"group\":\"主体\",\"params\":[\"品牌\",\"型号\",\"颜色\",\"上市年份\"]},{\"group\":\"网络\",\"params\":[\"4G网络制式\",\"3G网络制式\",\"2G网络制式\"]},{\"group\":\"存储\",\"params\":[\"机身内存\",\"储存卡类型\"]}]";
		List<Map> list = JsonUtils.jsonToList(paramData, Map.class);
		StringBuffer sb = new StringBuffer();
		sb.append("<table border=1>");
		for(Map map : list){
			String title = (String) map.get("group");
			List<String> a = (List<String>) map.get("params");
			sb.append("	<tr>");
			for (String item : a) {
				sb.append("		<td>"+title+"</td>");
				sb.append("		<td><input type='text' value='"+item+"' /></td>");
				sb.append("	</tr>");
			}
		}
		sb.append("</table>");
		System.out.println(sb);
	}
	
}
