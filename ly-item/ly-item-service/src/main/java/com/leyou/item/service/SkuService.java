package com.leyou.item.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leyou.item.dto.SkuDTO;
import com.leyou.item.entity.Sku;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


public interface SkuService extends IService<Sku> {

    List<SkuDTO> querySkuById(List<Long> ids);

    List<SkuDTO> querySkuBySpuId(Long id);
}