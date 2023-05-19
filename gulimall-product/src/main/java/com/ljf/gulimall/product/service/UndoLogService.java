package com.ljf.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ljf.common.utils.PageUtils;
import com.ljf.gulimall.product.entity.UndoLogEntity;

import java.util.Map;

/**
 * 
 *
 * @author ll
 * @email 861624779@qq.com
 * @date 2022-09-02 21:56:43
 */
public interface UndoLogService extends IService<UndoLogEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

