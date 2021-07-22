package com.leyou.item.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leyou.item.dto.SpuDetailDTO;
import com.leyou.item.entity.SpuDetail;


public interface SpuDetailService extends IService<SpuDetail> {
    SpuDetailDTO querySpuDetailById(Long id);
}