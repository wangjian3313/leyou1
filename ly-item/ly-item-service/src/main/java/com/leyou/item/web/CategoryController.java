package com.leyou.item.web;

import com.leyou.item.dto.CategoryDTO;
import com.leyou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @GetMapping("{id}")
    public ResponseEntity<CategoryDTO> queryCategoryById(@PathVariable("id") Long id){
        return ResponseEntity.ok(new CategoryDTO(categoryService.getById(id)));
    }
    /**
     * 根据分类id集合查询分类集合
     * @param ids
     * @return
     */
    @GetMapping("list")
    public ResponseEntity<List<CategoryDTO>> queryCategoryByIds(@RequestParam("ids") List<Long> ids){
        return ResponseEntity.ok(CategoryDTO.convertEntityList(categoryService.listByIds(ids)));
    }

    /**
     * 根据父类目id查询子类目的集合
     * @param pid 父类目的id
     * @return 子类目的集合
     */
    @GetMapping("/of/parent")
    public ResponseEntity<List<CategoryDTO>> queryCategoryByParentId(@RequestParam("pid") Long pid){
        return ResponseEntity.ok(// 封装并返回
                CategoryDTO.convertEntityList(// 把PO集合转为DTO集合
                        categoryService.query().eq("parent_id", pid).list()// 根据父类目id查询集合
                )
        );
    }

    /**
     * 根据品牌id查询分类集合
     * @param brandId 品牌id
     * @return 分类集合
     */
    @GetMapping("/of/brand")
    public ResponseEntity<List<CategoryDTO>> queryCategoryByBrandId(@RequestParam("id") Long brandId){
        return ResponseEntity.ok(categoryService.queryCategoryByBrandId(brandId));
    }
}