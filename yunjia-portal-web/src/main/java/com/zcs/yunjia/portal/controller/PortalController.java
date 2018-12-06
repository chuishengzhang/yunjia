package com.zcs.yunjia.portal.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zcs.yunjia.common.pojo.PortalCatResult;
import com.zcs.yunjia.common.utils.JsonUtils;
import com.zcs.yunjia.content.service.ContentService;
import com.zcs.yunjia.content.service.ItemCatService;
import com.zcs.yunjia.pojo.TbContent;
import com.zcs.yunjia.portal.pojo.SlideDate;


@Controller
public class PortalController {
	
	//注入service
	@Autowired
	private ContentService tbContentService;
	
	@Autowired
	private ItemCatService itemCatService;
	
	@Value(value = "${SLIDE_DATA_CATEGORYID}")
	private Long categortId;
	
	@Value(value = "${SLIDE_DATA_HEIGHT}")
	private Integer height;
	
	@Value(value = "${SLIDE_DATA_HEIGHTB}")
	private Integer heightB;
	
	@Value(value = "${SLIDE_DATA_WIDTH}")
	private Integer width;
	
	@Value(value = "${SLIDE_DATA_WIDTHB}")
	private Integer widthB;
	
	//显示首页
	@RequestMapping(value={"/index","/"})
	public String showIndex(Model model){
		List<TbContent> contentAds = tbContentService.getContentAd(categortId);
		List<SlideDate> ads = new ArrayList<SlideDate>();
		for(TbContent tc : contentAds){
			SlideDate data = new SlideDate();
			data.setSrcB(tc.getPic2());
			data.setHeight(height);
			data.setAlt(tc.getSubTitle());
			data.setWidth(width);
			data.setSrc(tc.getPic());
			data.setWidthB(widthB);
			data.setHref(tc.getUrl());
			data.setHeightB(heightB);
			ads.add(data);
		}
		System.out.println(categortId);
		model.addAttribute("ads",JsonUtils.objectToJson(ads));
		return "index";
	}
	
	@RequestMapping("/my")
	public String getMyIndex(){
		return "MyIndex";
	}
	
	/**
	 * 显示首页商品类目展示
	 * @param callback 回调函数
	 * @return jsonp js片段
	 */
	@RequestMapping(value="/portal/cat/list",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String getPortalCatList(@RequestParam("callback") String callback){
		PortalCatResult result = itemCatService.getItemCatList();
		String resultJson = JsonUtils.objectToJson(result);
		resultJson = callback+"("+resultJson+");";
		return resultJson;
	}
}
