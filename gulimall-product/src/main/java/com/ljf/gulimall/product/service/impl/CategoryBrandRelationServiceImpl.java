package com.ljf.gulimall.product.service.impl;

import com.ljf.gulimall.product.entity.BrandEntity;
import com.ljf.gulimall.product.entity.CategoryEntity;
import com.ljf.gulimall.product.service.BrandService;
import com.ljf.gulimall.product.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljf.common.utils.PageUtils;
import com.ljf.common.utils.Query;

import com.ljf.gulimall.product.dao.CategoryBrandRelationDao;
import com.ljf.gulimall.product.entity.CategoryBrandRelationEntity;
import com.ljf.gulimall.product.service.CategoryBrandRelationService;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Autowired
    CategoryBrandRelationDao categoryBrandRelationDao;
    @Autowired
    BrandService brandService;
    @Autowired
    CategoryService categoryService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<BrandEntity> getBrandsByCatId(Long catId) {
        List<CategoryBrandRelationEntity> catLog=categoryBrandRelationDao.selectList(new QueryWrapper<CategoryBrandRelationEntity>().eq("catelog_id",catId));
        List<BrandEntity> brandEntities=catLog.stream().map(item -> {
            BrandEntity brandEntity=new BrandEntity();
            long brandId= item.getBrandId();
            brandEntity=brandService.getById(brandId);
            return brandEntity;
        }).collect(Collectors.toList());
        return brandEntities;
    }

    @Override
    public List<CategoryEntity> getCatlogByBrandId(Long brandId) {
        List<CategoryBrandRelationEntity> catlog=categoryBrandRelationDao.selectList(new QueryWrapper<CategoryBrandRelationEntity>().eq("brand_id",brandId));
        List<CategoryEntity> entities=catlog.stream().map(item -> {
            CategoryEntity categoryEntity=new CategoryEntity();
            long catId= item.getCatelogId();
            categoryEntity=categoryService.getById(catId);
            return categoryEntity;
        }).collect(Collectors.toList());
        return entities;
    }

}