package com.leyou.search.service.impl;

import com.leyou.search.service.IndexService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@Controller
public class IndexServiceImplTest {

    @Autowired
    private IndexService indexService;

    @Test
    public void loadData() {
        this.indexService.loadData();
    }
}