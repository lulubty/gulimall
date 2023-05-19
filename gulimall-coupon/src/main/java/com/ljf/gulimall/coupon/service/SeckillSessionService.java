package com.ljf.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ljf.common.utils.PageUtils;
import com.ljf.gulimall.coupon.entity.SeckillSessionEntity;

import java.util.Map;

/**
 * 秒杀活动场次
 *
 * @author ll
 * @email 861624779@qq.com
 * @date 2022-09-02 23:33:22
 */
public interface SeckillSessionService extends IService<SeckillSessionEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

