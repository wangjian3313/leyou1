package com.leyou.item.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leyou.common.dto.PageDTO;
import com.leyou.item.dto.BrandDTO;
import com.leyou.item.entity.Brand;
import com.leyou.item.entity.CategoryBrand;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.service.BrandService;
import com.leyou.item.service.CategoryBrandService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements BrandService {

    private final CategoryBrandService categoryBrandService;


    public BrandServiceImpl(CategoryBrandService categoryBrandService) {
        this.categoryBrandService = categoryBrandService;
    }

    @Override
    public PageDTO<BrandDTO> findPage(Integer page, Integer rows, String key) {

        Page<Brand> brandPage = new Page<>(page,rows);
        QueryWrapper<Brand> wrapper = new QueryWrapper<Brand>()
                .like(StringUtils.isNotEmpty(key), "name", key)
                .or()
                .eq(StringUtils.isNotEmpty(key), "letter", key);
        this.page(brandPage,wrapper);
        List<Brand> brandList = brandPage.getRecords();
        return new PageDTO<>(brandPage.getTotal(), brandPage.getPages(), BrandDTO.convertEntityList(brandList));

    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public void addBrand(BrandDTO brandDTO, List<Long> categoryIds) {
        Brand brand = brandDTO.toEntity(Brand.class);
        this.save(brand);
        List<CategoryBrand> categoryBrandList = categoryIds.stream().map(id -> CategoryBrand.of(id, brand.getId())).collect(Collectors.toList());
        for (CategoryBrand categoryBrand : categoryBrandList) {
            categoryBrandService.save(categoryBrand);
        }
    }


    @Override
    public List<BrandDTO> queryBrandByIds(List<Long> ids) {
        return BrandDTO.convertEntityList( this.listByIds(ids));
    }

    @Override
    public List<BrandDTO> queryBrandByCategory(Long id) {

       List<CategoryBrand> categoryBrandList = categoryBrandService.query().eq("category_id",id).list();
        if(CollectionUtils.isEmpty(categoryBrandList)){
            return Collections.emptyList();
        }
        List<Long> brandIdList = categoryBrandList.stream().map(CategoryBrand::getBrandId).collect(Collectors.toList());
        List<Brand> brands = this.listByIds(brandIdList);
        return  BrandDTO.convertEntityList(brands);
    }

    @Override
    public BrandDTO queryBrandById(Long id) {
        return new BrandDTO(this.getById(id));
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public void deleteBrandById(Integer id) {
        this.categoryBrandService.remove(new QueryWrapper<CategoryBrand>().eq("brand_id",id));
        this.removeById(id);
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public void updateBrand(BrandDTO brandDTO, List<Long> categoryIds) {
        Brand brand = brandDTO.toEntity(Brand.class);
        this.update(brand,new QueryWrapper<Brand>().eq("id",brand.getId()));
        this.categoryBrandService.remove(new QueryWrapper<CategoryBrand>().eq("brand_id",brand.getId()));
        List<CategoryBrand> categoryBrandList = categoryIds.stream().map(id -> CategoryBrand.of(id, brand.getId())).collect(Collectors.toList());
        for (CategoryBrand categoryBrand : categoryBrandList) {
            categoryBrandService.save(categoryBrand);
        }
    }
}