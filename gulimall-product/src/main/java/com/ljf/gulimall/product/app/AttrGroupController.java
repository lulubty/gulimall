package com.ljf.gulimall.product.app;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.ljf.gulimall.product.entity.AttrEntity;
import com.ljf.gulimall.product.service.AttrAttrgroupRelationService;
import com.ljf.gulimall.product.service.AttrService;
import com.ljf.gulimall.product.vo.AttrGroupRelationVo;
import com.ljf.gulimall.product.vo.AttrGroupWithAttrsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ljf.gulimall.product.entity.AttrGroupEntity;
import com.ljf.gulimall.product.service.AttrGroupService;
import com.ljf.common.utils.PageUtils;
import com.ljf.common.utils.R;



/**
 * 属性分组
 *
 * @author ll
 * @email 861624779@qq.com
 * @date 2022-09-02 22:48:34
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;
    @Autowired
    private AttrService attrService;
    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;


    /**
     * 列表
     */
    @RequestMapping("/list/{catelogId}")
    //@RequiresPermissions("product:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params, @PathVariable("catelogId") Long catelogId){
        PageUtils page = attrGroupService.queryPage(params,catelogId);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    //@RequiresPermissions("product:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);

        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
   // @RequiresPermissions("product:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
   // @RequiresPermissions("product:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

    @RequestMapping("/attr/relation/delete")
    // @RequiresPermissions("product:attrgroup:delete")
    public R delete(@RequestBody AttrGroupRelationVo[] vos){
        attrService.deleteRelations(vos);

        return R.ok();
    }

    ///product/attrgroup/attr/relation
    @PostMapping("/attr/relation")
    public R addRelation(@RequestBody List<AttrGroupRelationVo> vos){

        attrAttrgroupRelationService.saveBatch(vos);
        return R.ok();
    }


    ///product/attrgroup/{attrgroupId}/attr/relation
    @GetMapping("/{attrgroupId}/attr/relation")
    public R attrRelation(@PathVariable("attrgroupId") Long attrgroupId){
        List<AttrEntity> entities =  attrService.getRelationAttr(attrgroupId);
        return R.ok().put("data",entities);
    }

    ///product/attrgroup/{attrgroupId}/noattr/relation
    @GetMapping("/{attrgroupId}/noattr/relation")
    public R attrNoRelation(@PathVariable("attrgroupId") Long attrgroupId,
                            @RequestParam Map<String, Object> params){
        PageUtils page = attrService.getNoRelationAttr(params,attrgroupId);
        return R.ok().put("page",page);
    }

    ///product/attrgroup/{attrgroupId}/attr/relation
    @GetMapping("/{catelogId}/withattr")
    public R attrRelationAndCatLog(@PathVariable("catelogId") Long cateLogId){
        List<AttrGroupWithAttrsVo> entites =  attrGroupService.getRelationAttrAndCatLog(cateLogId);
        return R.ok().put("data",entites);
    }
}
