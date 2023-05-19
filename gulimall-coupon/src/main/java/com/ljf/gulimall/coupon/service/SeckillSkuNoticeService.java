package com.ljf.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ljf.common.utils.PageUtils;
import com.ljf.gulimall.coupon.entity.SeckillSkuNoticeEntity;

import java.util.Map;

/**
 * 秒杀商品通知订阅
 *
 * @author ll
 * @email 861624779@qq.com
 * @date 2022-09-02 23:33:22
 */
public interface SeckillSkuNoticeService extends IService<SeckillSkuNoticeEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

