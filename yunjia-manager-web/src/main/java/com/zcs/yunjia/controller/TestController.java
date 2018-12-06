package com.zcs.yunjia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zcs.yunjia.service.TestService;


@Controller
public class TestController {
	
	//注入service
	@Autowired
	private TestService testService;
	
	@RequestMapping("/test/now")
	@ResponseBody
	public String queryNow(){
		return testService.queryNow();
	}
}
