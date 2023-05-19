package com.ljf.gulimall.product.app;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.ljf.gulimall.product.entity.BrandEntity;
import com.ljf.gulimall.product.entity.CategoryEntity;
import com.ljf.gulimall.product.vo.BrandVo;
import com.ljf.gulimall.product.vo.CatlogVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ljf.gulimall.product.entity.CategoryBrandRelationEntity;
import com.ljf.gulimall.product.service.CategoryBrandRelationService;
import com.ljf.common.utils.PageUtils;
import com.ljf.common.utils.R;



/**
 * 品牌分类关联
 *
 * @author ll
 * @email 861624779@qq.com
 * @date 2022-09-02 22:48:34
 */
@RestController
@RequestMapping("product/categorybrandrelation")
public class CategoryBrandRelationController {
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:categorybrandrelation:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = categoryBrandRelationService.queryPage(params);

        return R.ok().put("page", page);
    }
    @GetMapping("/bands/list")
    public R relationBrandsList(@RequestParam(value = "catId",required = true)Long catId){
        List<BrandEntity> vos = categoryBrandRelationService.getBrandsByCatId(catId);

        List<BrandVo> collect = vos.stream().map(item -> {
            BrandVo brandVo = new BrandVo();
            brandVo.setBrandId(item.getBrandId());
            brandVo.setBrandName(item.getName());

            return brandVo;
        }).collect(Collectors.toList());

        return R.ok().put("data",collect);

    }
    @GetMapping("/catelog/list")
    public R relationCatlogList(@RequestParam(value = "brandId",required = true)Long brandId){
        List<CategoryEntity> vos = categoryBrandRelationService.getCatlogByBrandId(brandId);

        List<CatlogVo> collect = vos.stream().map(item -> {
           CatlogVo vo=new CatlogVo();
           vo.setCatelogId(item.getCatId());
           vo.setCatelogName(item.getName());

            return vo;
        }).collect(Collectors.toList());

        return R.ok().put("data",collect);

    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("product:categorybrandrelation:info")
    public R info(@PathVariable("id") Long id){
		CategoryBrandRelationEntity categoryBrandRelation = categoryBrandRelationService.getById(id);

        return R.ok().put("categoryBrandRelation", categoryBrandRelation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:categorybrandrelation:save")
    public R save(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
		categoryBrandRelationService.save(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
   // @RequiresPermissions("product:categorybrandrelation:update")
    public R update(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
		categoryBrandRelationService.updateById(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
   // @RequiresPermissions("product:categorybrandrelation:delete")
    public R delete(@RequestBody Long[] ids){
		categoryBrandRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
