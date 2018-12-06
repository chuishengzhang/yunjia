package com.zcs.yunjia.common.pojo;

import java.io.IOException;
import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

import com.zcs.yunjia.common.utils.FileUtil;

/**
 * 封装上传文件的字节数组和后缀名
 * @author zcs
 */
public class UploadFile implements Serializable {
	
	public UploadFile(MultipartFile file){
		try {
			this.bytes = file.getBytes();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.extName = FileUtil.getMultiFileExtName(file);
	}
	
	private byte[] bytes; //字节数组
	private String extName; //后缀名
	public byte[] getBytes() {
		return bytes;
	}
	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
	public String getExtName() {
		return extName;
	}
	public void setExtName(String extName) {
		this.extName = extName;
	}
}
