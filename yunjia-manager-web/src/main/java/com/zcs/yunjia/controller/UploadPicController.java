package com.zcs.yunjia.controller;


import java.io.IOException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.zcs.yunjia.common.pojo.UploadFile;
import com.zcs.yunjia.common.pojo.UploadResult;
import com.zcs.yunjia.common.utils.JsonUtils;
import com.zcs.yunjia.service.UploadService;


/**
 * 上传图片Controller
 * @author zcs
 */

@Controller
public class UploadPicController {
	
	@Autowired
	private UploadService uploadService;
	
	@Value("${PIC_PREFIX}")
	private String PIC_PREFIX;
	
	@RequestMapping(value="/pic/upload")
	@ResponseBody
	public String uploadPic(MultipartFile uploadFile) throws IOException{
		
		//上传文件
		UploadFile upload = new UploadFile(uploadFile);
		UploadResult result = uploadService.upload(upload.getBytes(),upload.getExtName());
		return JsonUtils.objectToJson(result);
	}
}
