package com.leyou.search.web;



import com.leyou.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/goods")
public class SearchController {


    @Autowired
    private SearchService searchService;




    @GetMapping("/suggestion")
    public Mono<List<String>> suggest(@RequestParam("key")String key){
        return this.searchService.suggest(key);
    }
}