package com.ljf.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ljf.common.utils.PageUtils;
import com.ljf.gulimall.order.entity.OrderSettingEntity;

import java.util.Map;

/**
 * 订单配置信息
 *
 * @author ll
 * @email 861624779@qq.com
 * @date 2022-09-03 00:02:14
 */
public interface OrderSettingService extends IService<OrderSettingEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

