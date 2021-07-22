package com.leyou.item.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leyou.common.dto.PageDTO;
import com.leyou.item.dto.SpecParamDTO;
import com.leyou.item.dto.SpuDTO;
import com.leyou.item.entity.Spu;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface SpuService extends IService<Spu> {
   PageDTO<SpuDTO> querySpuByPage(Integer brandId, Integer cid3, Integer id, Integer page, Integer rows, Boolean saleable);

   void addGoos(SpuDTO spuDTO);

   SpuDTO querySpuDto(Integer id);

   void updateSpuSaleableById(Long id, Boolean saleable);

   void updateGoos(SpuDTO spuDTO);

   List<SpecParamDTO> queryParamsValues(Long id, Boolean searching);
}