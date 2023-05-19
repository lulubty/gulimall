package com.ljf.gulimall.order.dao;

import com.ljf.gulimall.order.entity.OrderItemEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单项信息
 * 
 * @author ll
 * @email 861624779@qq.com
 * @date 2022-09-03 00:02:14
 */
@Mapper
public interface OrderItemDao extends BaseMapper<OrderItemEntity> {
	
}
