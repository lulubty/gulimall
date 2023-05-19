package com.ljf.gulimall.product.service.impl;

import com.ljf.common.constant.ProductConstant;
import com.ljf.common.to.SkuHasStockVo;
import com.ljf.common.to.es.SkuEsModel;
import com.ljf.common.utils.R;
import com.ljf.gulimall.product.entity.*;
import com.ljf.gulimall.product.feign.SearchFeignService;
import com.ljf.gulimall.product.feign.WareFeignService;
import com.ljf.gulimall.product.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.TypeReference;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljf.common.utils.PageUtils;
import com.ljf.common.utils.Query;

import com.ljf.gulimall.product.dao.SpuInfoDao;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    SkuInfoService skuInfoService;
    @Autowired
    ProductAttrValueService productAttrValueService;
    @Autowired
    AttrService attrService;
    @Autowired
    WareFeignService wareFeignService;
    @Autowired
    BrandService brandService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    SearchFeignService searchFeignService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void up(long spuId) {
        //1、查出当前spuId对应的sku信息,品牌名字
        List<SkuInfoEntity> skus =  skuInfoService.getSkusBySpuId(spuId);
        List<Long> skuIdList = skus.stream().map(SkuInfoEntity::getSkuId).collect(Collectors.toList());
        //TODO 4、查询当前sku的所有可以被用来检索的规格属性
        List<ProductAttrValueEntity> baseAttrs = productAttrValueService.baseAttrListForSpu(spuId);
        List<Long> attrIds = baseAttrs.stream().map(attr -> {
            return attr.getAttrId();
        }).collect(Collectors.toList());

        List<Long> searchAttrIds = attrService.selectSearchAttrIds(attrIds);

        Set<Long> idSet = new HashSet<>(searchAttrIds);

        List<SkuEsModel.Attr> attrsList = baseAttrs.stream().filter(item -> {
            return idSet.contains(item.getAttrId());
        }).map(item -> {
            SkuEsModel.Attr attrs1 = new SkuEsModel.Attr();
            BeanUtils.copyProperties(item, attrs1);
            return attrs1;
        }).collect(Collectors.toList());

        //TODO 1、发送远程调用，库存系统查询是否有库存
        Map<Long, Boolean> stockMap = null;
        try {
            R r = wareFeignService.getSkusHasStock(skuIdList);
            TypeReference<List<SkuHasStockVo>> typeReference = new TypeReference<List<SkuHasStockVo>>() {};
            stockMap = r.getData(typeReference).stream().collect(Collectors.toMap(SkuHasStockVo::getSkuId, SkuHasStockVo::getHasStock));
        } catch (Exception e) {
            log.error("库存服务查询异常，原因：", e);
        }

        //2、封装每个sku的信息
        Map<Long, Boolean> finalStockMap = stockMap;
        List<SkuEsModel> upProducts = skus.stream().map(sku -> {
            //组装需要的数据
            SkuEsModel esModel = new SkuEsModel();
            BeanUtils.copyProperties(sku,esModel);
            esModel.setSkuPrice(sku.getPrice());
            esModel.setSkuImg(sku.getSkuDefaultImg());
            //hasStock,hotScore
            if(finalStockMap == null ){
                esModel.setHasStock(true);
            }else {
                esModel.setHasStock(finalStockMap.get(sku.getSkuId()));
            }
            //TODO 2、热度评分。0
            esModel.setHotScore(0L);
            //TODO 3、查询品牌和分类的名字信息
            BrandEntity brand = brandService.getById(esModel.getBrandId());
            esModel.setBrandName(brand.getName());
            esModel.setBrandImg(brand.getLogo());
            CategoryEntity category = categoryService.getById(esModel.getCatalogId());
            esModel.setCatalogName(category.getName());
            //设置检索属性
            esModel.setAttrs(attrsList);
            return esModel;
        }).collect(Collectors.toList());

        //TODO 5、将数据发送给es进行保存
        R r = searchFeignService.productStatusUp(upProducts);
        if (r.getCode() == 0){
            //远程调用成功
            //TODO 6、修改当前spu的状态
           this.baseMapper.updateSpuStatus(spuId, ProductConstant.ProductStatusEnum.SPU_UP.getCode());
        }else {
            //远程调用失败
            //TODO 7、重复调用？接口幂等性；重试机制
            //Feign调用流程
            /**
             * 1、构造请求数据，将对象转为json
             *      RequestTemplate template = buildTemplateFromArgs.create(argv);
             * 2、发送请求进行执行（执行成功会解码响应数据）
             *      executeAndDecode(template)
             * 3、执行请求会有重试机制
             *      while(true){
             *          try{
             *              executeAndDecode(template);
             *          }catch(){
             *              retryer.continueOrPropagate(e);
             *              throw ex;
             *              continue;
             *          }
             *      }
             */
        }

    }



}