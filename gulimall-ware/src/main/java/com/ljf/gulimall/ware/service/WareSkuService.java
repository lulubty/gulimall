package com.ljf.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ljf.common.utils.PageUtils;
import com.ljf.gulimall.ware.entity.WareSkuEntity;
import com.ljf.common.to.SkuHasStockVo;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author ll
 * @email 861624779@qq.com
 * @date 2022-09-03 00:11:39
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void addStock(Long skuId, Long wareId, Integer skuNum);

    List<SkuHasStockVo> getSkusHasStock(List<Long> skuIds);
}

