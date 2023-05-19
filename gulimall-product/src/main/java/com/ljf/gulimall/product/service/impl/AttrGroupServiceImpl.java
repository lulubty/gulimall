package com.ljf.gulimall.product.service.impl;

import com.ljf.gulimall.product.dao.AttrDao;
import com.ljf.gulimall.product.service.AttrService;
import com.ljf.gulimall.product.vo.AttrGroupWithAttrsVo;
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

import com.ljf.gulimall.product.dao.AttrGroupDao;
import com.ljf.gulimall.product.entity.AttrGroupEntity;
import com.ljf.gulimall.product.service.AttrGroupService;
import org.springframework.util.StringUtils;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    AttrGroupDao attrGroupDao;
    @Autowired
    AttrDao attrDao;
    @Autowired
    AttrService attrService;
    @Override
    public PageUtils queryPage(Map<String, Object> params,Long catelogId) {
        String key = (String) params.get("key");
        //select * from pms_attr_group where catelog_id=? and (attr_group_id=key or attr_group_name like %key%)
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<AttrGroupEntity>();
        if(!StringUtils.isEmpty(key)){
            wrapper.and((obj)->{
                obj.eq("attr_group_id",key).or().like("attr_group_name",key);
            });
        }

        if( catelogId == 0){
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params),
                    wrapper);
            return new PageUtils(page);
        }else {
            wrapper.eq("catelog_id",catelogId);
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params),
                    wrapper);
            return new PageUtils(page);
        }
    }

    @Override
    public  List<AttrGroupWithAttrsVo> getRelationAttrAndCatLog(Long cateLogId) {

       List<AttrGroupEntity> attrGroupEntities= attrGroupDao.selectList(new QueryWrapper<AttrGroupEntity>().eq("catelogId",cateLogId));
       List<AttrGroupWithAttrsVo> ans=attrGroupEntities.stream().map(item ->{
           AttrGroupWithAttrsVo attrsVo=new AttrGroupWithAttrsVo();
           BeanUtils.copyProperties(item,attrsVo);
           attrsVo.setAttrs(attrService.getRelationAttr(item.getAttrGroupId()));
           return attrsVo;
       }).collect(Collectors.toList());
        return ans;
    }

}