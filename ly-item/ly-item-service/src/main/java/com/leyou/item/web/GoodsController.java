package com.leyou.item.web;

import com.leyou.common.dto.PageDTO;
import com.leyou.item.dto.SkuDTO;
import com.leyou.item.dto.SpuDTO;
import com.leyou.item.dto.SpuDetailDTO;
import com.leyou.item.service.SkuService;
import com.leyou.item.service.SpuDetailService;
import com.leyou.item.service.SpuService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    private final SpuService spuService;

    private final SpuDetailService detailService;

    private final SkuService skuService;

    public GoodsController(SpuService SpuService, SpuDetailService detailService, SkuService skuService) {
        this.spuService = SpuService;
        this.detailService = detailService;
        this.skuService = skuService;
    }
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
        return ResponseEntity.ok(this.spuService.querySpuByPage(brandId,cid3,id,page,rows,saleable));
    }

    /**
     * 新增商品
     */

    @PostMapping("/spu")
    public ResponseEntity<Void> addGoos(@RequestBody SpuDTO spuDTO){
        this.spuService.addGoos(spuDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 根据id批量查询sku
     */
    @GetMapping("/sku/list")
    public ResponseEntity<List<SkuDTO>> querySkuById(@RequestParam("ids") List<Long> ids){
        return ResponseEntity.ok(this.skuService.querySkuById(ids));
    }

    /**
     * 根据spuID查询sku的集合
     */
    @GetMapping("/sku/of/spu")
    public ResponseEntity<List<SkuDTO>> querySkuBySpuId(@RequestParam("id")Integer id){
        return  ResponseEntity.ok(this.skuService.querySkuBySpuId(id));
    }
    /**
     * 根据id查询商品详情
     */
    @GetMapping("spu/detail")
    public ResponseEntity<SpuDetailDTO> querySpuDetailById(@RequestParam("id") Integer id){
        return ResponseEntity.ok(this.detailService.querySpuDetailById(id));
    }
    /**
     * 根据spuId查询spu的所有规格参数值
     */
 //   @GetMapping("/spec/value")


}