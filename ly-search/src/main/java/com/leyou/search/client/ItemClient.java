package com.leyou.search.client;


import com.leyou.common.dto.PageDTO;
import com.leyou.item.dto.SkuDTO;
import com.leyou.item.dto.SpecParamDTO;
import com.leyou.item.dto.SpuDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Feign的原理: 对 http 请求的伪装
 * 需要知道：localhost:8081/goods/spu/page?page=1
 * - 主机和端口：通过@FeignClient("item-service")得到服务名称，去eureka根据服务名称拉取服务列表
 * - 请求方式： @GetMapping
 * - 请求路径：@GetMapping("/goods/spu/page")
 * - 请求参数：@RequestParam(value = "page", defaultValue = "1") Integer page
 * - 返回值类型：响应体的类型
 */
@FeignClient("item-service")
public interface ItemClient {

    /**
     *spu分页查询以及其他动态查询
     */
    @GetMapping("/goods/spu/page")
    PageDTO<SpuDTO> querySpuByPage(
            @RequestParam(value = "brandId", required = false) Integer brandId,
            @RequestParam(value = "categoryId", required = false) Integer cid3,
            @RequestParam(value = "id", required = false) Integer id,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable", required = false) Boolean saleable
    );

    /**
     * 根据spuID查询sku的集合
     */
    @GetMapping("/goods/sku/of/spu")
    List<SkuDTO> querySkuBySpuId(@RequestParam("id")Long id);


    /**
     * 根据spuId查询spu的所有规格参数值
     */
    @GetMapping("/goods/spec/value")
    List<SpecParamDTO> queryParamsValues(
            @RequestParam(value = "id",required = false) Long id,
            @RequestParam(value = "searching",required = false) Boolean searching);
}
