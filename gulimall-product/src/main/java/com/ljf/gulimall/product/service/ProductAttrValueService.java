package com.ljf.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ljf.common.utils.PageUtils;
import com.ljf.gulimall.product.entity.ProductAttrValueEntity;

import java.util.List;
import java.util.Map;

/**
 * spu属性值
 *
 * @author ll
 * @email 861624779@qq.com
 * @date 2022-09-02 21:56:42
 */
public interface ProductAttrValueService extends IService<ProductAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<ProductAttrValueEntity> baseAttrlistforspu(Long spuId);

    void updateAttrSpu(long spuId,List<ProductAttrValueEntity> entities);

    List<ProductAttrValueEntity> baseAttrListForSpu(long spuId);
}

