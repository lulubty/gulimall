package com.ljf.gulimall.product.dao;

import com.ljf.gulimall.product.entity.SpuInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * spu信息
 * 
 * @author ll
 * @email 861624779@qq.com
 * @date 2022-09-02 21:56:42
 */
@Mapper
public interface SpuInfoDao extends BaseMapper<SpuInfoEntity> {

@Update( " update pms_spu_info set publish_status=#{code},update_time=NOW() where id =#{spuId}")
    void updateSpuStatus(@Param("spuId") Long spuId, @Param("code") int code);
}
