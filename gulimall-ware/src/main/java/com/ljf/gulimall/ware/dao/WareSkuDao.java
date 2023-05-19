package com.ljf.gulimall.ware.dao;

import com.ljf.gulimall.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 商品库存
 * 
 * @author ll
 * @email 861624779@qq.com
 * @date 2022-09-03 00:11:39
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {

    void addStock(@Param("sku_id") Long skuId,@Param("ware_id") Long wareId, @Param("sku_num") Integer skuNum);


    @Select( "select sum(stock - stock_locked) from wms_ware_sku where sku_id=#{sku_id}")
    Long getSkuStock(Long skuId);
}
