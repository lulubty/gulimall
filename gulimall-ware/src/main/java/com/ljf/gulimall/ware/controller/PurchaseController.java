package com.ljf.gulimall.ware.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.ljf.gulimall.ware.vo.MergeVo;
import com.ljf.gulimall.ware.vo.PurchaseDoneVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ljf.gulimall.ware.entity.PurchaseEntity;
import com.ljf.gulimall.ware.service.PurchaseService;
import com.ljf.common.utils.PageUtils;
import com.ljf.common.utils.R;



/**
 * 采购信息
 *
 * @author ll
 * @email 861624779@qq.com
 * @date 2022-09-03 00:11:39
 */
@RestController
@RequestMapping("ware/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("ware:purchase:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("ware:purchase:info")
    public R info(@PathVariable("id") Long id){
		PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("ware:purchase:save")
    public R save(@RequestBody PurchaseEntity purchase){
		purchaseService.save(purchase);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
   // @RequiresPermissions("ware:purchase:update")
    public R update(@RequestBody PurchaseEntity purchase){
		purchaseService.updateById(purchase);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
   // @RequiresPermissions("ware:purchase:delete")
    public R delete(@RequestBody Long[] ids){
		purchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }
    /**
     * 合并采购需求
     */
    @RequestMapping("/merge")
    // @RequiresPermissions("ware:purchase:update")
    public R mergePurchase(@RequestBody MergeVo mergeVo){

        purchaseService.mergePurchase(mergeVo);
        return R.ok();


    }
    /**
     * 领取采购单
     * @return
     */
    @PostMapping("/received")
    public R received(@RequestBody List<Long> ids){

        purchaseService.received(ids);

        return R.ok();
    }

    @RequestMapping("/unreceive/list")
    //@RequiresPermissions("ware:purchase:list")
    public R unreceivelist(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPageUnreceivePurchase(params);

        return R.ok().put("page", page);
    }

    ///ware/purchase/done
    @PostMapping("/done")
    public R finish(@RequestBody PurchaseDoneVo doneVo){

        purchaseService.done(doneVo);

        return R.ok();
    }

}
