package com.ljf.gulimall.ware.service.impl;

import com.ljf.common.constant.WareConstant;
import com.ljf.gulimall.ware.entity.PurchaseDetailEntity;
import com.ljf.gulimall.ware.service.PurchaseDetailService;
import com.ljf.gulimall.ware.service.WareSkuService;
import com.ljf.gulimall.ware.vo.MergeVo;
import com.ljf.gulimall.ware.vo.PurchaseDoneVo;
import com.ljf.gulimall.ware.vo.PurchaseItemDoneVo;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljf.common.utils.PageUtils;
import com.ljf.common.utils.Query;

import com.ljf.gulimall.ware.dao.PurchaseDao;
import com.ljf.gulimall.ware.entity.PurchaseEntity;
import com.ljf.gulimall.ware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Autowired
    PurchaseDetailService purchaseDetailService;
    @Autowired
    WareSkuService wareSkuService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }
    @Transactional
    @Override
    public void mergePurchase(MergeVo mergeVo) {
        Long purchaseId = mergeVo.getPurchaseId();
        if(purchaseId == null){
            //1、新建一个
            PurchaseEntity purchaseEntity = new PurchaseEntity();

            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.CREATED.getCode());
            purchaseEntity.setCreateTime(new Date());
            purchaseEntity.setUpdateTime(new Date());
            this.save(purchaseEntity);
            purchaseId = purchaseEntity.getId();
        }

        //TODO 确认采购单状态是0,1才可以合并

        List<Long> items = mergeVo.getItems();
        Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> collect = items.stream().map(i -> {
            PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();

            detailEntity.setId(i);
            detailEntity.setPurchaseId(finalPurchaseId);
            detailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode());
            return detailEntity;
        }).collect(Collectors.toList());


        purchaseDetailService.updateBatchById(collect);

        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(purchaseId);
        purchaseEntity.setUpdateTime(new Date());
        this.updateById(purchaseEntity);

    }

    @Override
    public void received(List<Long> ids) {
        //1、确认当前采购单是新建或者已分配状态
        List<PurchaseEntity> collect = ids.stream().map(id -> {
            PurchaseEntity byId = this.getById(id);
            return byId;
        }).filter(item -> {
            if (item.getStatus() == WareConstant.PurchaseStatusEnum.CREATED.getCode() ||
                    item.getStatus() == WareConstant.PurchaseStatusEnum.ASSIGNED.getCode()) {
                return true;
            }
            return false;
        }).map(item->{
            item.setStatus(WareConstant.PurchaseStatusEnum.RECEIVE.getCode());
            item.setUpdateTime(new Date());
            return item;
        }).collect(Collectors.toList());

        //2、改变采购单的状态
        this.updateBatchById(collect);



        //3、改变采购项的状态
        collect.forEach((item)->{
            List<PurchaseDetailEntity> entities = purchaseDetailService.listDetailByPurchaseId(item.getId());
            List<PurchaseDetailEntity> detailEntities = entities.stream().map(entity -> {
                PurchaseDetailEntity entity1 = new PurchaseDetailEntity();
                entity1.setId(entity.getId());
                entity1.setStatus(WareConstant.PurchaseDetailStatusEnum.BUYING.getCode());
                return entity1;
            }).collect(Collectors.toList());
            purchaseDetailService.updateBatchById(detailEntities);
        });
    }

    @Override
    public PageUtils queryPageUnreceivePurchase(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>().eq("status",0).or().eq("status",1)
        );

        return new PageUtils(page);
    }


    @Transactional
    @Override
    public void done(PurchaseDoneVo doneVo) {

       boolean flag=false;


        //3、改变采购项的状态
       List<PurchaseDetailEntity> detailEntities=new ArrayList<>();
        List<PurchaseItemDoneVo> items = doneVo.getItems();
            for(PurchaseItemDoneVo entity:items) {
                PurchaseDetailEntity entity1 = new PurchaseDetailEntity();
                entity1.setId(entity.getItemId());
                entity1.setStatus(entity.getStatus());
                ////3、无异常则将成功采购的进行入库
                if (entity.getStatus() == WareConstant.PurchaseDetailStatusEnum.FINISH.getCode()) {

                    PurchaseDetailEntity item = purchaseDetailService.getById(entity.getItemId());
                    wareSkuService.addStock(item.getSkuId(), item.getWareId(), item.getSkuNum());
                }else{
                    flag=true;
                }
               detailEntities.add(entity1);
            }
        purchaseDetailService.updateBatchById(detailEntities);

        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(doneVo.getId());
        purchaseEntity.setStatus(flag?WareConstant.PurchaseStatusEnum.HASERROR.getCode() : WareConstant.PurchaseStatusEnum.FINISH.getCode());;
        purchaseEntity.setUpdateTime(new Date());


        //、改变采购单的状态
        this.updateById(purchaseEntity);


    }

}