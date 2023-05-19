package com.ljf.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ljf.common.utils.PageUtils;
import com.ljf.gulimall.ware.entity.UndoLogEntity;

import java.util.Map;

/**
 * 
 *
 * @author ll
 * @email 861624779@qq.com
 * @date 2022-09-03 00:11:39
 */
public interface UndoLogService extends IService<UndoLogEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

