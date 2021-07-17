package com.leyou.item.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leyou.common.dto.PageDTO;
import com.leyou.item.dto.SkuDTO;
import com.leyou.item.dto.SpuDTO;
import com.leyou.item.entity.Category;
import com.leyou.item.entity.Sku;
import com.leyou.item.entity.Spu;
import com.leyou.item.entity.SpuDetail;
import com.leyou.item.mapper.SpuMapper;
import com.leyou.item.service.BrandService;
import com.leyou.item.service.CategoryService;
import com.leyou.item.service.SpuDetailService;
import com.leyou.item.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpuServiceImpl extends ServiceImpl<SpuMapper, Spu> implements SpuService {

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SpuDetailService spuDetailService;

    @Autowired
    private SkuServiceImpl skuService;

    /**
     * 分页查询spu
     */
    @Override
    public PageDTO<SpuDTO> querySpuByPage(Integer brandId, Integer cid3, Integer id, Integer page1, Integer rows, Boolean saleable) {

        IPage<Spu> spuPage = new Page<>(page1,rows);

        QueryWrapper<Spu> spuDTOQueryWrapper = new QueryWrapper<Spu>()
                .eq(saleable != null, "saleable", saleable)
                .eq(cid3 != null, "cid3", cid3)
                .eq(brandId != null, "brand_id", brandId)
                .eq(id != null, "id", id);


       this.page(spuPage,spuDTOQueryWrapper).getRecords();

        List<SpuDTO> spuDTOS = SpuDTO.convertEntityList( this.page(spuPage,spuDTOQueryWrapper).getRecords());
        for (SpuDTO spuDTO : spuDTOS) {
            spuDTO.setBrandName(this.brandService.queryBrandById(spuDTO.getBrandId()).getName());
            List<Long> categoryIds = spuDTO.getCategoryIds();
            spuDTO.setCategoryName(this.categoryService.listByIds(categoryIds).stream().map(category -> category.getName()).collect(Collectors.joining()));
        }
        return new PageDTO<SpuDTO>(spuPage.getTotal(),spuPage.getPages(),spuDTOS);
    }

    /**
     * 商品添加
     * @param spuDTO
     */
    @Override
    @Transactional
    public void addGoos(SpuDTO spuDTO) {
        Spu spu = spuDTO.toEntity(Spu.class);
        this.save(spu);

        SpuDetail spuDetail = spuDTO.getSpuDetail().toEntity(SpuDetail.class);
        spuDetail.setSpuId(spu.getId());
        this.spuDetailService.save(spuDetail);

        List<SkuDTO> skus = spuDTO.getSkus();
        for (SkuDTO skuDTO : skus) {
            Sku sku = skuDTO.toEntity(Sku.class);
            sku.setSpuId(spu.getId());
            this.skuService.save(sku);
        }
    }

}