package com.ljf.gulimall.search.controller;




import com.ljf.common.exception.BizCodeEnum;
import com.ljf.common.to.es.SkuEsModel;
import com.ljf.common.utils.R;
import com.ljf.gulimall.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description: ElasticSaveController
 * @Author: WangTianShun
 * @Date: 2020/11/4 19:37
 * @Version 1.0
 */
@RequestMapping("/search/save")
@RestController
@Slf4j
public class ElasticSaveController {

    @Autowired
    ProductSaveService productSaveService;
    //上架商品
    @PostMapping("product")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels){
        boolean b = false;
        try{
            b = productSaveService.productStatusUp(skuEsModels);
        }catch(Exception e){
            log.error("ElasticSaveController商品上架错误：{}",e);
            return R.error(BizCodeEnum.PRODUCT_UP_EXCEPTION.getCode(),BizCodeEnum.PRODUCT_UP_EXCEPTION.getMsg());
        }

        if (!b){
            return R.ok();
        }else {
            return R.error(BizCodeEnum.PRODUCT_UP_EXCEPTION.getCode(),BizCodeEnum.PRODUCT_UP_EXCEPTION.getMsg());
        }

    }
}
