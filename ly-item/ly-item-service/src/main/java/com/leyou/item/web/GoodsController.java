package com.leyou.item.web;

import com.leyou.common.dto.PageDTO;
import com.leyou.item.dto.SkuDTO;
import com.leyou.item.dto.SpecParamDTO;
import com.leyou.item.dto.SpuDTO;
import com.leyou.item.dto.SpuDetailDTO;
import com.leyou.item.service.GoodsService;
import com.leyou.item.service.SkuService;
import com.leyou.item.service.SpuDetailService;
import com.leyou.item.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private  GoodsService goodsService;


    /**
     * 分页查询spu
     */
    @GetMapping("/spu/page")
    public ResponseEntity<PageDTO<SpuDTO>> querySpuByPage(
            @RequestParam(value = "brandId",required = false) Integer brandId,
            @RequestParam(value = "categoryId",required = false) Integer cid3,
            @RequestParam(value = "id",required = false) Integer id,
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "rows",defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable",required = false) Boolean saleable
    ){
        return ResponseEntity.ok(this.goodsService.querySpuByPage(brandId,cid3,id,page,rows,saleable));
    }

    /**
     * 新增商品
     */

    @PostMapping("/spu")
    public ResponseEntity<Void> addGoos(@RequestBody SpuDTO spuDTO){
        this.goodsService.addGoos(spuDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 更新商品
     */
    @PutMapping("/spu")
    public ResponseEntity<Void> updateGoos(@RequestBody SpuDTO spuDTO){
        this.goodsService.updateGoos(spuDTO);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    /**
     * 根据id批量查询sku
     */
    @GetMapping("/sku/list")
    public ResponseEntity<List<SkuDTO>> querySkuById(@RequestParam("ids") List<Long> ids){
        return ResponseEntity.ok(this.goodsService.querySkuById(ids));
    }

    /**
     * 根据spuID查询sku的集合
     */
    @GetMapping("/sku/of/spu")
    public ResponseEntity<List<SkuDTO>> querySkuBySpuId(@RequestParam("id")Long id){
        return  ResponseEntity.ok(this.goodsService.querySkuBySpuId(id));
    }
    /**
     * 根据id查询商品详情
     */
    @GetMapping("spu/detail")
    public ResponseEntity<SpuDetailDTO> querySpuDetailById(@RequestParam("id") Long id){
        return ResponseEntity.ok(this.goodsService.querySpuDetailById(id));
    }
    /**
     * 根据id查询商品
     */
    @GetMapping("/spu/{id}")
    public ResponseEntity<SpuDTO> querySpuDto(@PathVariable("id") Integer id){
        return ResponseEntity.ok(this.goodsService.querySpuDto(id));
    }


    /**
     * 根据id查询spu及sku、spuDetail等
     */
    @GetMapping("/{id}")
    public ResponseEntity<SpuDTO> queryGoods(@PathVariable("id") Integer id){
        return ResponseEntity.ok(this.goodsService.queryGoods(id));
    }

    /**
     *修改商品上下架
     */
    @PutMapping("/saleable")
    public ResponseEntity<Void> updateSpuSaleableById(
            @RequestParam("id") Long id
            ,@RequestParam("saleable") Boolean saleable){
        this.goodsService.updateSpuSaleableById(id,saleable);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 根据spuId查询spu的所有规格参数值
     */
    @GetMapping("/spec/value")
    public ResponseEntity<List<SpecParamDTO>> queryParamsValues(
            @RequestParam(value = "id",required = false) Long id,
            @RequestParam(value = "searching",required = false) Boolean searching){
        return ResponseEntity.ok(this.goodsService.queryParamsValues(id,searching));
    }


}