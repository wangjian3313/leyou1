package com.leyou.item.service;


import com.leyou.common.dto.PageDTO;
import com.leyou.item.dto.SkuDTO;
import com.leyou.item.dto.SpecParamDTO;
import com.leyou.item.dto.SpuDTO;
import com.leyou.item.dto.SpuDetailDTO;

import java.util.List;

public interface GoodsService {

    PageDTO<SpuDTO> querySpuByPage(Integer brandId, Integer cid3, Integer id, Integer page, Integer rows, Boolean saleable);

    void addGoos(SpuDTO spuDTO);

    List<SkuDTO> querySkuById(List<Long> ids);

    List<SkuDTO> querySkuBySpuId(Long id);

    SpuDetailDTO querySpuDetailById(Long id);

    SpuDTO querySpuDto(Integer id);

    SpuDTO queryGoods(Integer id);

    void updateSpuSaleableById(Long id, Boolean saleable);

    void updateGoos(SpuDTO spuDTO);

    List<SpecParamDTO> queryParamsValues(Long id, Boolean searching);
}
