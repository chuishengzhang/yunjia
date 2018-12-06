package com.zcs.yunjia.common.pojo;

import java.io.Serializable;
import java.util.List;

//实现序列化接口  因为该类需要在系统间传输
/**
 * easyui datagrid 返回结果的pojo类
 *
 */
public class DataGridResult implements Serializable {
	
	private Integer total;//总个数
	private List rows;//item集合
	
	
	public DataGridResult() {
		super();
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public List getRows() {
		return rows;
	}
	public void setRows(List rows) {
		this.rows = rows;
	}
	
	
}
