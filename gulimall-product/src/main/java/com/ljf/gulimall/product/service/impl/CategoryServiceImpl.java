package com.ljf.gulimall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ljf.gulimall.product.vo.Catelog2Vo;
import lombok.Synchronized;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljf.common.utils.PageUtils;
import com.ljf.common.utils.Query;

import com.ljf.gulimall.product.dao.CategoryDao;
import com.ljf.gulimall.product.entity.CategoryEntity;
import com.ljf.gulimall.product.service.CategoryService;
import org.springframework.util.StringUtils;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {
 @Autowired
 CategoryDao categoryDao;
 @Autowired
 StringRedisTemplate redisTemplate;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        //查出所有分类
        List<CategoryEntity> entities=baseMapper.selectList(null);
        //组装成父子的树形结构
        List<CategoryEntity> level1Menus=entities.stream().filter(categoryEntity ->
                categoryEntity.getParentCid() == 0
        ).map((menu)->{
            menu.setChildren(getChildrens(menu,entities));
            return menu;
        }).sorted((menu1,menu2)->{
            return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 :menu2.getSort());
        }).collect(Collectors.toList());
        //找到所有的一级分类
        return level1Menus;
    }
    private List<CategoryEntity> getChildrens(CategoryEntity root,List<CategoryEntity> all){
        List<CategoryEntity> children = all.stream().filter(categoryEntity -> {
            return categoryEntity.getParentCid() == root.getCatId();
        }).map(categoryEntity -> {
            //1、找到子菜单
            categoryEntity.setChildren(getChildrens(categoryEntity, all));
            return categoryEntity;
            //2、菜单的排序
        }).sorted((menu1, menu2) -> {
            return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 :menu2.getSort());
        }).collect(Collectors.toList());


        return children;

    }
    @Override
    public   void removeMenuByIds(List<Long> asList) {
        //TODO 检查当前的菜单是否被别的地方所引用
        categoryDao.deleteBatchIds(asList);
    }

    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> paths = new ArrayList<>();
        List<Long> parentPath = findParentPath(catelogId, paths);

        Collections.reverse(parentPath);


        return parentPath.toArray(new Long[parentPath.size()]);

    }

    @Override
    public List<CategoryEntity> getParentCid(List<CategoryEntity> selectList ,long parentCid) {
        List<CategoryEntity> categoryEntities = selectList.stream().filter(item->item.getParentCid()==parentCid).collect(Collectors.toList());
        return categoryEntities;
    }
    @Override
    public Map<String, List<Catelog2Vo>> getCatelogJson() {
        //给缓存中放json字符串，拿出的json字符串，反序列为能用的对象

        /**
         * 1、空结果缓存：解决缓存穿透问题
         * 2、设置过期时间(加随机值)：解决缓存雪崩
         * 3、加锁：解决缓存击穿问题
         */

        //1、加入缓存逻辑,缓存中存的数据是json字符串
        //JSON跨语言。跨平台兼容。
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String catalogJson = ops.get("catalogJson");
        if (StringUtils.isEmpty(catalogJson)) {
            System.out.println("缓存不命中...查询数据库...");
            //2、缓存中没有数据，查询数据库
            Map<String, List<Catelog2Vo>> catalogJsonFromDb = getCatelogJsonFromDB();

            return catalogJsonFromDb;
        }

        System.out.println("缓存命中...直接返回...");
        //转为指定的对象
        Map<String, List<Catelog2Vo>> result = JSON.parseObject(catalogJson,new TypeReference<Map<String, List<Catelog2Vo>>>(){});

        return result;
    }



    /**
         * 逻辑是
         * （1）根据一级分类，找到对应的二级分类
         * （2）将得到的二级分类，封装到Catelog2Vo中
         * （3）根据二级分类，得到对应的三级分类
         * （3）将三级分类封装到Catalog3List
         * @return
         */


        public Map<String, List<Catelog2Vo>> getCatelogJsonFromDB() {
           // synchronized 分布式锁
            //得到锁以后，我们应该再去缓存中确定一次，如果没有才需要继续查询
            String catalogJson = redisTemplate.opsForValue().get("catalogJson");
            if (!StringUtils.isEmpty(catalogJson)) {
                //缓存不为空直接返回
                Map<String, List<Catelog2Vo>> result = JSON.parseObject(catalogJson, new TypeReference<Map<String, List<Catelog2Vo>>>() {
                });

                return result;
            }

            System.out.println("查询了数据库");

            /**
             * 将数据库的多次查询变为一次
             */

            List<CategoryEntity> selectList=this.baseMapper.selectList(null);
            //查出所有1级分类
            List<CategoryEntity> level1Category = getParentCid(selectList,0l);

            //2、封装数据
            Map<String, List<Catelog2Vo>> parent_cid = level1Category.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
                //1、每一个的一级分类，查到这个一级分类的二级分类
                List<CategoryEntity> categoryEntities = getParentCid(selectList,v.getCatId());
                //2、封装上面的结果
                List<Catelog2Vo> catelog2Vos = null;
                if (categoryEntities != null) {
                    catelog2Vos = categoryEntities.stream().map(l2 -> {
                        Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName());
                        //1、找当前二级分类的三级分类封装vo
                        List<CategoryEntity> level3Catelog = getParentCid(selectList,l2.getCatId());
                        if (level3Catelog != null){
                            List<Catelog2Vo.Category3Vo> collect = level3Catelog.stream().map(l3 -> {
                                //2、封装成指定格式
                                Catelog2Vo.Category3Vo catelog3Vo = new Catelog2Vo.Category3Vo(l2.getCatId().toString(), l3.getCatId().toString(), l3.getName());
                                return catelog3Vo;
                            }).collect(Collectors.toList());
                            catelog2Vo.setCatalog3List(collect);
                        }
                        return catelog2Vo;
                    }).collect(Collectors.toList());
                }
                return catelog2Vos;
            }));
            //3、将查到的数据放入缓存,将对象转为json
            String valueJson = JSON.toJSONString(parent_cid);
            //设定过期时间
            redisTemplate.opsForValue().set("catalogJson", valueJson, 1, TimeUnit.DAYS);

            return parent_cid;
        }


    private List<Long> findParentPath(Long catelogId, List<Long> paths) {
        paths.add(catelogId);
        CategoryEntity categoryEntity=this.getById(catelogId);
        if(categoryEntity.getParentCid()!=0){
            findParentPath(categoryEntity.getParentCid(),paths);
        }
        return paths;
    }

}