package com.ljf.gulimall.product.web;

import com.ljf.gulimall.product.entity.CategoryEntity;
import com.ljf.gulimall.product.service.CategoryService;
import com.ljf.gulimall.product.vo.Catelog2Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class IndexController {
    @Autowired
    CategoryService categoryService;
    @GetMapping({"/", "index.html"})
    public String getIndex(Model model) {
        //获取所有的一级分类

        List<CategoryEntity> catagories = categoryService.getLevel1Catagories();
        model.addAttribute("catagories", catagories);
        return "index";
    }
    @ResponseBody
    @GetMapping("/index/json/catalog.json")
    public Map<String, List<Catelog2Vo>> getCatelogJson(){
        Map<String, List<Catelog2Vo>> catelogJson = categoryService.getCatelogJson();
        return catelogJson;
    }
}
