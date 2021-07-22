package com.leyou.search.repository;

import com.leyou.search.entity.Goods;
import com.leyou.starter.elastic.repository.Repository;
import org.springframework.stereotype.Component;

@Component
public interface GoodsRepository extends Repository<Goods, Long> {
}