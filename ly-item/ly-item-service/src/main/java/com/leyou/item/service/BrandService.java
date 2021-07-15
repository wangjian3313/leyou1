package com.leyou.item.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leyou.common.dto.PageDTO;
import com.leyou.item.dto.BrandDTO;
import com.leyou.item.entity.Brand;

import java.util.List;


public interface BrandService extends IService<Brand> {
    PageDTO<BrandDTO> findPage(Integer page, Integer rows, String key);

    void addBrand(BrandDTO brandDTO,List<Long>categoryIds);

    List<BrandDTO> queryBrandByIds(List<Long> ids);

    List<BrandDTO> queryBrandByCategory(Long id);

    BrandDTO queryBrandById(Long id);

    void deleteBrandById(Integer id);

    void updateBrand(BrandDTO brandDTO, List<Long> categoryIds);
}