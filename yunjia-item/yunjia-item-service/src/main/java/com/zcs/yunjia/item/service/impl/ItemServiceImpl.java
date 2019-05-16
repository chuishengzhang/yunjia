package com.zcs.yunjia.item.service.impl;

import com.zcs.yunjia.item.service.ItemService;
import com.zcs.yunjia.mapper.TbItemMapper;
import com.zcs.yunjia.pojo.TbItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemServiceImpl  implements ItemService {

    @Autowired
    private TbItemMapper itemMapper;

    /**
     * @param id 查询商品的id
     */
    public TbItem getItemById(Long id){
        return itemMapper.selectByPrimaryKey(id);
    }
}
