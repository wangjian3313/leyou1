package com.leyou.item.web;

import com.leyou.common.dto.PageDTO;
import com.leyou.item.dto.BrandDTO;
import com.leyou.item.service.BrandService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/brand")
public class BrandController {

    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    /**
     * 分页查询
     */
    @GetMapping("/page")
    public ResponseEntity<PageDTO<BrandDTO>> findPage( @RequestParam(value = "page",defaultValue = "1")  Integer page,
                                                       @RequestParam(value = "rows" ,defaultValue = "5") Integer rows,
                                                       @RequestParam(value = "key", required = false) String key){

        return ResponseEntity.ok(brandService.findPage(page,rows,key));
    }

    /**
     * 新增品牌
     */
    @PostMapping
    public ResponseEntity<Void> addBrand(BrandDTO brandDTO,@RequestParam("categoryIds") List<Long> categoryIds) {
        brandService.addBrand(brandDTO,categoryIds);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 根据品牌id集合查询品牌集合
     */
    @GetMapping("/list")
    public ResponseEntity<List<BrandDTO>> queryBrandByIds(@RequestParam("ids") List<Long> ids){
        return ResponseEntity.ok(brandService.queryBrandByIds(ids));
    }
    /**
     * 根据分类id查询品牌
     */
    @GetMapping("/of/category")
    public ResponseEntity<List<BrandDTO>> queryBrandByCategory(@RequestParam("id") Long id){
        return ResponseEntity.ok(brandService.queryBrandByCategory(id));
    }
    /**
     * 根据id查询品牌
     */
    @GetMapping("/brand/{id}")
    public ResponseEntity<BrandDTO> queryBrandById(@PathVariable("id") Long id){
        return ResponseEntity.ok(brandService.queryBrandById(id));
    }

    /**
     * 根据id删除品牌
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrandById(@PathVariable("id") Integer id){
        this.brandService.deleteBrandById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    /**
     * 更新商品
     */
    @PutMapping
    public ResponseEntity<Void> updateBrand(BrandDTO brandDTO,@RequestParam("categoryIds") List<Long> categoryIds) {
        brandService.updateBrand(brandDTO,categoryIds);
        return ResponseEntity.status(HttpStatus. NO_CONTENT).build();
    }

}