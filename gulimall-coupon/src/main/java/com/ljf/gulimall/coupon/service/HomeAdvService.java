package com.ljf.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ljf.common.utils.PageUtils;
import com.ljf.gulimall.coupon.entity.HomeAdvEntity;

import java.util.Map;

/**
 * 首页轮播广告
 *
 * @author ll
 * @email 861624779@qq.com
 * @date 2022-09-02 23:33:22
 */
public interface HomeAdvService extends IService<HomeAdvEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

