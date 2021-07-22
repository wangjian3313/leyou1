package com.leyou.item.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leyou.common.dto.PageDTO;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.JsonUtils;
import com.leyou.item.dto.SkuDTO;
import com.leyou.item.dto.SpecParamDTO;
import com.leyou.item.dto.SpuDTO;
import com.leyou.item.entity.Category;
import com.leyou.item.entity.Sku;
import com.leyou.item.entity.Spu;
import com.leyou.item.entity.SpuDetail;
import com.leyou.item.mapper.SpuMapper;
import com.leyou.item.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    @Autowired
    private SpecParamService specParamService;



    /**
     * 分页查询spu
     */
    @Override
    public PageDTO<SpuDTO> querySpuByPage(Integer brandId, Integer cid3, Integer id, Integer page1, Integer rows, Boolean saleable) {

        IPage<Spu> spuPage = new Page<>(page1, rows);

        QueryWrapper<Spu> spuDTOQueryWrapper = new QueryWrapper<Spu>()
                .eq(saleable != null, "saleable", saleable)
                .eq(cid3 != null, "cid3", cid3)
                .eq(brandId != null, "brand_id", brandId)
                .eq(id != null, "id", id);


        this.page(spuPage, spuDTOQueryWrapper).getRecords();

        List<SpuDTO> spuDTOS = SpuDTO.convertEntityList(this.page(spuPage, spuDTOQueryWrapper).getRecords());
        /*for (SpuDTO spuDTO : spuDTOS) {
            spuDTO.setBrandName(this.brandService.queryBrandById(spuDTO.getBrandId()).getName());
           // List<Long> categoryIds = spuDTO.getCategoryIds();
            spuDTO.setCategoryName(this.categoryService.listByIds(spuDTO.getCategoryIds()).stream().map(category -> category.getName()).collect(Collectors.joining()));
        } */
        spuDTOS.forEach(spuDTO -> {
            spuDTO.setBrandName(this.brandService.queryBrandById(spuDTO.getBrandId()).getName());
            spuDTO.setCategoryName(this.categoryService.listByIds(spuDTO.getCategoryIds()).stream().map(Category::getName).collect(Collectors.joining("/")));
        });
        return new PageDTO<SpuDTO>(spuPage.getTotal(), spuPage.getPages(), spuDTOS);
    }

    /**
     * 商品添加
     *
     * @param spuDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
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


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateGoos(SpuDTO spuDTO) {
        //要修改spu
        if (spuDTO.getId() != null) {
            this.updateById(spuDTO.toEntity(Spu.class));
        }
        //要修改spuDetail
        if (null != spuDTO.getSpuDetail()) {
            this.spuDetailService.updateById(spuDTO.getSpuDetail().toEntity(SpuDetail.class));
        }
        //要修改sku,三种可能，新增，删除，修改
        if (!CollectionUtils.isEmpty(spuDTO.getSkus())) {
            List<Sku> toADD = new ArrayList<>();
            List<Sku> toDelete = new ArrayList<>();
            List<Sku> toUpdate = new ArrayList<>();
            //遍历传递来的sku的集合，分离，
            spuDTO.getSkus().forEach(skuDTO -> {
                Sku sku = skuDTO.toEntity(Sku.class);
                //没有id，表示要新增
                if (sku.getId() == null) {
                    toADD.add(sku);
                    //saleable为空表示要修改
                } else if (sku.getSaleable() != null) {
                    toDelete.add(sku);
                    //saleable不为空，表示要删除
                } else {

                    toUpdate.add(sku);
                }
            });

            if (!CollectionUtils.isEmpty(toADD)) {
                this.skuService.saveBatch(toADD);
            }

            if (!CollectionUtils.isEmpty(toDelete)) {
                this.skuService.removeByIds(toDelete.stream().map(Sku::getId).collect(Collectors.toList()));
            }

            if (!CollectionUtils.isEmpty(toUpdate)) {
                this.skuService.updateBatchById(toUpdate);
            }

        }

    }

    @Override

    public List<SpecParamDTO> queryParamsValues(Long id, Boolean searching) {

        //获取spu
        List<SpecParamDTO> specParamDTOS = this.specParamService.querySpecParams(this.getById(id).getCid3().intValue(), null, searching);
        Map<Long, Object> map = JsonUtils.toMap( this.spuDetailService.getById(id).getSpecification(), Long.class, Object.class);
        specParamDTOS.forEach(specParamDTO ->  specParamDTO.setValue(map.get(specParamDTO.getId())));
        return specParamDTOS;
    }

    @Override
    public SpuDTO querySpuDto(Integer id) {
        return new SpuDTO(this.getById(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSpuSaleableById(Long id, Boolean saleable) {
       /* UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("id", id);
        updateWrapper.set("saleable", saleable);*/
        // this.update(updateWrapper);
        Spu spu = new Spu();
        spu.setId(id);
        spu.setSaleable(saleable);
        this.updateById(spu);

        List<SkuDTO> skuDTOS = this.skuService.querySkuBySpuId(id);
        if (CollectionUtils.isEmpty(skuDTOS)) {
            throw new LyException(204, "暂时未上架sku");
        }
        List<Sku> skuList = skuDTOS.stream().map(skuDTO -> {
            Sku sku = skuDTO.toEntity(Sku.class);
            sku.setSaleable(saleable);
            return sku;
        }).collect(Collectors.toList());

        this.skuService.updateBatchById(skuList);

    }


}