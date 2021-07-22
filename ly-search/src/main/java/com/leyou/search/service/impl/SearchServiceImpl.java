package com.leyou.search.service.impl;

import com.leyou.search.repository.GoodsRepository;
import com.leyou.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private GoodsRepository goodsRepository;

    @Override
    public Mono<List<String>> suggest(String key) {

       return this.goodsRepository.suggestBySingleField("suggestion",key);
    }
}
