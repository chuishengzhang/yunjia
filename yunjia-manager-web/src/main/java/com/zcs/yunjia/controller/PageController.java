package com.zcs.yunjia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 负责显示页面的Controller
 * 
 * @author zcs
 *
 */
@Controller
public class PageController {

	// 显示首页
	@RequestMapping("/")
	public String showIndex() {
		return "index";
	}

	// 显示各个子页 将请求路径当作参数放回
	// 请求item-list 返回item-list
	// 请求item-add 返回item-add
	@RequestMapping(value={"/{page}","/rest/page/{page}"})
	public String showPage(@PathVariable String page) {
		return page;
	}
}
