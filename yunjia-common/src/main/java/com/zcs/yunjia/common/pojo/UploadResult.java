package com.zcs.yunjia.common.pojo;

import java.io.Serializable;

/**
 * kindeditor 插件上传文件放回参数的pojo
 * @author 1
 */
/*	kindeditor官方文档上要求的返回格式
 * 成功时
	{
	        "error" : 0,
	        "url" : "http://www.example.com/path/to/file.ext"
	}
	失败时
	{
	        "error" : 1,
	        "message" : "错误信息"
	}*/
public class UploadResult implements Serializable{
	
	private int error;
	private String url;
	private String message;
	public int getError() {
		return error;
	}
	public void setError(int error) {
		this.error = error;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
