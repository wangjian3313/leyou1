package com.leyou.item.service.impl;

import com.leyou.common.dto.PageDTO;
import com.leyou.common.exception.LyException;
import com.leyou.item.dto.SkuDTO;
import com.leyou.item.dto.SpecParamDTO;
import com.leyou.item.dto.SpuDTO;
import com.leyou.item.dto.SpuDetailDTO;
import com.leyou.item.service.GoodsService;
import com.leyou.item.service.SkuService;
import com.leyou.item.service.SpuDetailService;
import com.leyou.item.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Description:
 * @Author: Qi
 * @Date: 11:52 2021/7/18
 */
@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private  SpuService spuService;

    @Autowired
    private  SpuDetailService detailService;

    @Autowired
    private  SkuService skuService;

    @Override
    public PageDTO<SpuDTO> querySpuByPage(Integer brandId, Integer cid3, Integer id, Integer page, Integer rows, Boolean saleable) {
        return this.spuService.querySpuByPage(brandId,cid3,id,page,rows,saleable);
    }

    @Override
    public void addGoos(SpuDTO spuDTO) {
        this.spuService.addGoos(spuDTO);
    }

    @Override
    public List<SkuDTO> querySkuById(List<Long> ids) {
        return this.skuService.querySkuById(ids);
    }

    @Override
    public List<SkuDTO> querySkuBySpuId(Long id) {
        return this.skuService.querySkuBySpuId(id);
    }

    @Override
    public SpuDetailDTO querySpuDetailById(Long id) {
        return this.detailService.querySpuDetailById(id);
    }

    @Override
    public SpuDTO querySpuDto(Integer id) {
        return this.spuService.querySpuDto(id);
    }

    @Override
    public SpuDTO queryGoods(Integer id) {
        PageDTO<SpuDTO> spuDTOPageDTO = this.spuService.querySpuByPage(null, null, id, 1, 1, null);
        if(spuDTOPageDTO!=null&& !CollectionUtils.isEmpty(spuDTOPageDTO.getItems())){
            SpuDTO spuDTO = spuDTOPageDTO.getItems().get(0);
            spuDTO.setSpuDetail(this.detailService.querySpuDetailById(spuDTO.getId()));
            spuDTO.setSkus(this.skuService.querySkuBySpuId(spuDTO.getId()));
            return spuDTO;
        }else {
            throw new LyException(204,"你查询的id不存在");
        }
    }

    @Override
    public void updateSpuSaleableById(Long id, Boolean saleable) {
        this.spuService.updateSpuSaleableById(id,saleable);
    }

    @Override
    public void updateGoos(SpuDTO spuDTO) {
        this.spuService.updateGoos(spuDTO);
    }

    @Override
    public List<SpecParamDTO> queryParamsValues(Long id, Boolean searching) {
        return this.spuService.queryParamsValues(id,searching);
    }
}
