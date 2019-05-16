package com.zcs.yunjia.item.service;

import com.zcs.yunjia.pojo.TbItem;

/**
 * 商品信息接口
 */
public interface ItemService {
    //根据商品id查询商品
    public abstract  TbItem getItemById(Long id);
}
