package com.zcs.yunjia.common.pojo;

import java.io.Serializable;

/**
 * 封装easyui tree数据的pojo
 *
 */
public class EasyUITreeNode implements Serializable {
	private Long id; //节点id
	private String text; //要显示的节点文本
	//节点状态，'open' 或 'closed'，默认是 'open'。
	//当设置为 'closed' 时，该节点有子节点，并且将从远程站点加载它们。
	private String state;
	
	
	public EasyUITreeNode() {
		super();
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
	}


	public String getState() {
		return state;
	}


	public void setState(String state) {
		this.state = state;
	}		
}

