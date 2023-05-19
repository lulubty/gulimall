package com.ljf.gulimall.product.feign;

import com.ljf.common.to.es.SkuEsModel;
import com.ljf.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Description: SearchFeignService
 * @Author: WangTianShun
 * @Date: 2020/11/4 20:11
 * @Version 1.0
 */

@FeignClient("gulimall-search")
public interface SearchFeignService {

    @PostMapping("search/save/product")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels);
}
