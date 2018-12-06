package com.zcs.yunjia.common.pojo;

import java.io.Serializable;


/**
 * 封装内容分类pojo 
 * data 新增分类的数据
 * status 状态
 *
 */
public class RequestResult implements Serializable {
	private Object data;
	private Integer status;
	
	
	public RequestResult() {
		super();
	}
	
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
}
