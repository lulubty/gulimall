package com.ljf.gulimall.product.feign;

import com.ljf.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;


import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Description: WareFeignService
 * @Author: WangTianShun
 * @Date: 2020/11/4 19:03
 * @Version 1.0
 */

@FeignClient("gulimall-ware")
public interface WareFeignService {
    /**
     * 1、R设计的时候可以加上泛型
     * 2、直接返回我们想要的结果
     * 3、自己封装返回结果
     * @param skuIds
     * @return
     */
    @PostMapping("/ware/waresku/hasStock")
    R getSkusHasStock(@RequestBody List<Long> skuIds);
}
