package com.leyou.item.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leyou.item.dto.SkuDTO;
import com.leyou.item.entity.Sku;
import com.leyou.item.mapper.SkuMapper;
import com.leyou.item.service.SkuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service
public class SkuServiceImpl extends ServiceImpl<SkuMapper, Sku> implements SkuService {

    @Override
    public List<SkuDTO> querySkuById(List<Long> ids) {
        return SkuDTO.convertEntityList(this.listByIds(ids));
    }

    @Override
    public List<SkuDTO> querySkuBySpuId(Integer id) {

        return SkuDTO.convertEntityList( this.list(new QueryWrapper<Sku>().eq("spu_id",id)));
    }
}