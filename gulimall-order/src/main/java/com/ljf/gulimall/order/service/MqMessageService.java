package com.ljf.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ljf.common.utils.PageUtils;
import com.ljf.gulimall.order.entity.MqMessageEntity;

import java.util.Map;

/**
 * 
 *
 * @author ll
 * @email 861624779@qq.com
 * @date 2022-09-03 00:02:14
 */
public interface MqMessageService extends IService<MqMessageEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

