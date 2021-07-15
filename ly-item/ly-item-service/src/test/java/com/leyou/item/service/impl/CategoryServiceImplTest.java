package com.leyou.item.service.impl;

import com.leyou.item.entity.Category;
import com.leyou.item.service.CategoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CategoryServiceImplTest {

    @Autowired
    private CategoryService categoryService;

    @Test
    public void queryCategoryById() {
    }

    @Test
    public void queryCategoryByIds() {
    }

    @Test
    public void queryCategoryByParentId() {
    }

    @Test
    public void queryCategoryByBrandId() {
    }
}