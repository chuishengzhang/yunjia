package com.zcs.yunjia.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zcs.yunjia.pojo.TbItemParam;
import com.zcs.yunjia.pojo.TbItemParamExample;
import com.zcs.yunjia.pojo.TbItemParamWithCatName;
import com.zcs.yunjia.pojo.TbItemParamWithCatNameExample;

public interface TbItemParamMapper {
    int countByExample(TbItemParamExample example);

    int deleteByExample(TbItemParamExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TbItemParam record);

    int insertSelective(TbItemParam record);

    List<TbItemParam> selectByExampleWithBLOBs(TbItemParamExample example);

    List<TbItemParam> selectByExample(TbItemParamExample example);

    TbItemParam selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TbItemParam record, @Param("example") TbItemParamExample example);

    int updateByExampleWithBLOBs(@Param("record") TbItemParam record, @Param("example") TbItemParamExample example);

    int updateByExample(@Param("record") TbItemParam record, @Param("example") TbItemParamExample example);

    int updateByPrimaryKeySelective(TbItemParam record);

    int updateByPrimaryKeyWithBLOBs(TbItemParam record);

    int updateByPrimaryKey(TbItemParam record);
    
    List<TbItemParamWithCatName> selectItemParamWithItemName(TbItemParamWithCatNameExample example);
}