package com.ljf.gulimall.product.app;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.ljf.gulimall.product.entity.ProductAttrValueEntity;
import com.ljf.gulimall.product.service.ProductAttrValueService;
import com.ljf.gulimall.product.vo.AttrRespVo;
import com.ljf.gulimall.product.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ljf.gulimall.product.service.AttrService;
import com.ljf.common.utils.PageUtils;
import com.ljf.common.utils.R;



/**
 * 商品属性
 *
 * @author ll
 * @email 861624779@qq.com
 * @date 2022-09-02 22:48:34
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    @Autowired
    ProductAttrValueService productAttrValueService;
    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:attr:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    //@RequiresPermissions("product:attr:info")
    public R info(@PathVariable("attrId") Long attrId){
		AttrRespVo attrRespVo = attrService.getAttrVo(attrId);

        return R.ok().put("attr", attrRespVo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attr:save")
    public R save(@RequestBody AttrVo attr){
        attrService.saveAttr(attr);

        return R.ok();
    }

    // /product/attr/base/listforspu/{spuId}
    @GetMapping("/base/listforspu/{spuId}")
    public R baseAttrlistforspu(@PathVariable("spuId") Long spuId){

        List<ProductAttrValueEntity> entities = productAttrValueService.baseAttrlistforspu(spuId);

        return R.ok().put("data",entities);
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
   // @RequiresPermissions("product:attr:update")
    public R update(@RequestBody AttrVo attr){
		attrService.updateAttr(attr);

        return R.ok();
    }
    /**
     * 修改商品规格
     */
    @RequestMapping("/update/{spuId}")
    // @RequiresPermissions("product:attr:update")
    public R updateAttrSpu(@PathVariable("spuId") long spuId,@RequestBody List<ProductAttrValueEntity> entities){
        productAttrValueService.updateAttrSpu(spuId,entities);

        return R.ok();
    }
    /**
     * 删除
     */
    @RequestMapping("/delete")
   // @RequiresPermissions("product:attr:delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }


    @RequestMapping("/{attrType}/list/{catelogId}")
    public R getSaleAttr(@RequestParam Map<String, Object> params,@PathVariable("catelogId") long catelogId,@PathVariable("attrType") String type){
        PageUtils page=attrService.querySalePage(params,catelogId,type);
        return R.ok().put("page",page);
    }
}
