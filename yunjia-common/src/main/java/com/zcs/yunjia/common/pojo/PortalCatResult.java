package com.zcs.yunjia.common.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 首页分类数据的pojo
 * @author zcs
 *
 */
public class PortalCatResult implements Serializable{
	
	private List data;

	public List getData() {
		return data;
	}

	public void setData(List data) {
		this.data = data;
	}
	
	
}
