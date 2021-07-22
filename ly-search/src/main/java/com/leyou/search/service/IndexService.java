package com.leyou.search.service;

import com.leyou.item.dto.SpuDTO;
import com.leyou.search.entity.Goods;

/**
 * @Description:
 * @Author: Qi
 * @Date: 19:34 2021/7/21
 */
public interface IndexService {
    void loadData();

    Goods buildGoods(SpuDTO spuDTO);
}
