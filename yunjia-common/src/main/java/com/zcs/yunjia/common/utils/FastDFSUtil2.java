package com.zcs.yunjia.common.utils;

import java.io.IOException;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.web.multipart.MultipartFile;

/**
 * FastDFS client
 * 上传图片工具类
 * @author zcs
 */
public class FastDFSUtil2 {
	
	public String uploadFile(String configPath,MultipartFile picFile) throws IOException, MyException{
		
		//String path = this.getClass().getClassLoader().getResource(configPath).getFile();
		//1.加载配置文件
		ClientGlobal.init(configPath);
		//1.创建TrackerClient
		TrackerClient trackerClient = new TrackerClient();
		
		TrackerServer trackerServer = trackerClient.getConnection();
		
		StorageServer storageServer = null;
		
		StorageClient storageClient = new StorageClient(trackerServer, storageServer);
		
		//取后缀名
		String originalFilename = picFile.getOriginalFilename();
		int dotIndex = originalFilename.lastIndexOf(".");
		String fix = originalFilename.substring(dotIndex+1);
		
		String[] upload_file = storageClient.upload_file(picFile.getBytes(),fix, null);
		
		//拼接url
		String url = upload_file[0]+"/"+upload_file[1];
		
		return url;
	}
	
public String uploadFile(String configPath,byte[] bytes,String extName) throws IOException, MyException{
		
		//String path = this.getClass().getClassLoader().getResource(configPath).getFile();
		//1.加载配置文件
		ClientGlobal.init(configPath);
		//1.创建TrackerClient
		TrackerClient trackerClient = new TrackerClient();
		
		TrackerServer trackerServer = trackerClient.getConnection();
		
		StorageServer storageServer = null;
		
		StorageClient storageClient = new StorageClient(trackerServer, storageServer);
		
		String[] upload_file = storageClient.upload_file(bytes,extName, null);
		
		//拼接url
		String url = upload_file[0]+"/"+upload_file[1];
		
		return url;
	}
}
