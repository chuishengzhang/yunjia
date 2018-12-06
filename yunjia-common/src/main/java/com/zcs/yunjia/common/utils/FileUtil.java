package com.zcs.yunjia.common.utils;


import org.springframework.web.multipart.MultipartFile;

/**
 * 文件工具类
 * @author zcs
 *
 */
public class FileUtil {
	
	/**
	 * 获取MultipartFile文件后缀名
	 */
	public static String getMultiFileExtName(MultipartFile file){
		//取后缀名
		String originalFilename = file.getOriginalFilename();
		int dotIndex = originalFilename.lastIndexOf(".");
		String extName = originalFilename.substring(dotIndex+1);
		return extName;
	}
}
