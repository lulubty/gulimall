package com.ljf.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ljf.common.utils.PageUtils;
import com.ljf.gulimall.product.entity.BrandEntity;
import com.ljf.gulimall.product.entity.CategoryBrandRelationEntity;
import com.ljf.gulimall.product.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author ll
 * @email 861624779@qq.com
 * @date 2022-09-02 21:56:43
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<BrandEntity> getBrandsByCatId(Long catId);

    List<CategoryEntity> getCatlogByBrandId(Long brandId);
}

