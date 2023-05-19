package com.ljf.gulimall.search.service;

import com.ljf.common.to.es.SkuEsModel;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface ProductSaveService {
    boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException;


}
