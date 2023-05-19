package com.ljf.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ljf.common.utils.PageUtils;
import com.ljf.gulimall.product.entity.CategoryEntity;
import com.ljf.gulimall.product.vo.Catelog2Vo;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author ll
 * @email 861624779@qq.com
 * @date 2022-09-02 21:56:43
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryEntity> listWithTree();

    void removeMenuByIds(List<Long> asList);

    Long[] findCatelogPath(Long catelogId);

    List<CategoryEntity> getParentCid( List<CategoryEntity> selectList,long parentCid);

    Map<String,List<Catelog2Vo>> getCatelogJson();
}

