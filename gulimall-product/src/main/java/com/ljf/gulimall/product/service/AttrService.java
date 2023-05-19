package com.ljf.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ljf.common.utils.PageUtils;
import com.ljf.gulimall.product.entity.AttrEntity;
import com.ljf.gulimall.product.vo.AttrGroupRelationVo;
import com.ljf.gulimall.product.vo.AttrRespVo;
import com.ljf.gulimall.product.vo.AttrVo;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author ll
 * @email 861624779@qq.com
 * @date 2022-09-02 21:56:43
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);


    void saveAttr(AttrVo attr);

    List<AttrEntity> getRelationAttr(Long attrgroupId);

    PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId);

    AttrRespVo getAttrVo(Long attrId);

    void updateAttr(AttrVo attr);

    PageUtils querySalePage(Map<String, Object> params, long catelogId, String attrType);

    void deleteRelations(AttrGroupRelationVo[] vos);

    List<Long> selectSearchAttrIds(List<Long> attrIds);
}

