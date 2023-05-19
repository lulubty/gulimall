package com.ljf.gulimall.product.dao;

import com.ljf.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author ll
 * @email 861624779@qq.com
 * @date 2022-09-02 21:56:43
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
