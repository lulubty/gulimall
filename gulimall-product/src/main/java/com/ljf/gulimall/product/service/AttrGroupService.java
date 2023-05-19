package com.ljf.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ljf.common.utils.PageUtils;
import com.ljf.gulimall.product.entity.AttrGroupEntity;
import com.ljf.gulimall.product.vo.AttrGroupWithAttrsVo;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author ll
 * @email 861624779@qq.com
 * @date 2022-09-02 21:56:43
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params,Long cateLogId);

    List<AttrGroupWithAttrsVo> getRelationAttrAndCatLog(Long cateLogId);
}

