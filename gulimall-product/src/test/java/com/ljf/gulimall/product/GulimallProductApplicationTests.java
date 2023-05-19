package com.ljf.gulimall.product;

import com.ljf.gulimall.product.entity.BrandEntity;
import com.ljf.gulimall.product.service.BrandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GulimallProductApplicationTests {
   @Autowired
    BrandService brandService;
    @Test
    void contextLoads() {
       BrandEntity brandEntity = new BrandEntity();
        brandEntity.setBrandId(13l);
        brandEntity.setDescript("增加");
        brandService.updateById(brandEntity);


    }

}
