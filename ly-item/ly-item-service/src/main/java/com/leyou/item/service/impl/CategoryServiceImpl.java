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

    public CategoryServiceImpl(CategoryBrandService categoryBrandService) {
        this.categoryBrandService = categoryBrandService;
    }

    /**
     * 根据id查询分类
     */
    @Override
    public CategoryDTO queryCategoryById(Long id) {
        return new CategoryDTO(this.getById(id));
    }

    /**
     *根据分类id的集合，查询商品分类的集合
     */
    @Override
    public List<CategoryDTO> queryCategoryByIds(List<Long> ids) {
        return CategoryDTO.convertEntityList( this.listByIds(ids));
    }

    /**
     *根据父类目id，查询子类目的集合
     */
    @Override
    public List<CategoryDTO> queryCategoryByParentId(Long pid) {
        return CategoryDTO.convertEntityList(this.query().eq("parent_id", pid).list());
    }

    /**
     * 根据品牌id，查询商品分类的集合
     */
    @Override
    public List<CategoryDTO> queryCategoryByBrandId(Long brandId){
        // 1.根据品牌id，查询中间表，得到中间表对象集合
        List<CategoryBrand> categoryBrandList = categoryBrandService.query().eq("brand_id", brandId).list();
        if(CollectionUtils.isEmpty(categoryBrandList)){
            return Collections.emptyList();
        }
        // 2.获取分类id集合
        List<Long> categoryIdList = categoryBrandList.stream()
                .map(CategoryBrand::getCategoryId).collect(Collectors.toList());
        // 3.根据分类id集合，查询分类对象集合
        List<Category> categories = listByIds(categoryIdList);
        // 4.转换DTO
        return CategoryDTO.convertEntityList(categories);
    }

  
}