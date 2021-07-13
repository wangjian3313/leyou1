package com.leyou.item.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leyou.item.dto.CategoryDTO;
import com.leyou.item.entity.Category;
import com.leyou.item.entity.CategoryBrand;
import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.service.CategoryBrandService;
import com.leyou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    private final CategoryBrandService categoryBrandService;

    @Autowired
    public CategoryServiceImpl(CategoryBrandService categoryBrandService) {
        this.categoryBrandService = categoryBrandService;
    }

    @Override
    public List<CategoryDTO> queryCategoryByBrandId(Long brandId){
       //根据品牌Id查询中间表,获取中间表对象集合
        List<CategoryBrand> categoryBrandList = categoryBrandService.query().eq("brand_id", brandId).list();
        if(CollectionUtils.isEmpty(categoryBrandList)){
            return Collections.emptyList();
        }
        //根据中间表对象集合获取分类id集合
        List<Long> list = categoryBrandList.stream().map(CategoryBrand::getBrandId).collect(Collectors.toList());
        List<Category> categoriesList = this.listByIds(list);
        //将PO类型转换为DTO类型
        return CategoryDTO.convertEntityList(categoriesList);
    }
}