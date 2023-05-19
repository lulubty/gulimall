package com.ljf.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ljf.common.utils.PageUtils;
import com.ljf.gulimall.ware.entity.PurchaseEntity;
import com.ljf.gulimall.ware.vo.MergeVo;
import com.ljf.gulimall.ware.vo.PurchaseDoneVo;

import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author ll
 * @email 861624779@qq.com
 * @date 2022-09-03 00:11:39
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void mergePurchase(MergeVo mergeVo);

    void received(List<Long> ids);

    PageUtils queryPageUnreceivePurchase(Map<String, Object> params);

    void done(PurchaseDoneVo doneVo);
}

