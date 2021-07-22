package com.leyou.search.service;

import reactor.core.publisher.Mono;

import java.util.List;

public interface SearchService {
    Mono<List<String>> suggest(String key);
}
