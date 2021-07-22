package com.leyou.search.service.impl;

import com.leyou.common.dto.PageDTO;
import com.leyou.common.exception.LyException;
import com.leyou.item.dto.SkuDTO;
import com.leyou.item.dto.SpecParamDTO;
import com.leyou.item.dto.SpuDTO;
import com.leyou.search.client.ItemClient;
import com.leyou.search.entity.Goods;
import com.leyou.search.repository.GoodsRepository;
import com.leyou.search.service.IndexService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: Qi
 * @Date: 19:34 2021/7/21
 */
@Slf4j
@Service
public class IndexServiceImpl implements IndexService {

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private ItemClient itemClient;

    @Override
    public void loadData() {
        //先清理索引库
        try {
            //删除索引库
            this.goodsRepository.deleteIndex();
        } catch (Throwable e) {
        }

        //索引库重建
        this.goodsRepository.createIndex("{\n" +
                "  \"settings\": {\n" +
                "    \"analysis\": {\n" +
                "      \"analyzer\": {\n" +
                "        \"my_pinyin\": {\n" +
                "          \"tokenizer\": \"ik_smart\",\n" +
                "          \"filter\": [\n" +
                "            \"py\"\n" +
                "          ]\n" +
                "        }\n" +
                "      },\n" +
                "      \"filter\": {\n" +
                "        \"py\": {\n" +
                "\t\t  \"type\": \"pinyin\",\n" +
                "          \"keep_full_pinyin\": true,\n" +
                "          \"keep_joined_full_pinyin\": true,\n" +
                "          \"keep_original\": true,\n" +
                "          \"limit_first_letter_length\": 16,\n" +
                "          \"remove_duplicated_term\": true\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  },\n" +
                "  \"mappings\": {\n" +
                "    \"properties\": {\n" +
                "      \"id\": {\n" +
                "        \"type\": \"keyword\"\n" +
                "      },\n" +
                "      \"suggestion\": {\n" +
                "        \"type\": \"completion\",\n" +
                "        \"analyzer\": \"my_pinyin\",\n" +
                "        \"search_analyzer\": \"ik_smart\"\n" +
                "      },\n" +
                "      \"title\":{\n" +
                "        \"type\": \"text\",\n" +
                "        \"analyzer\": \"my_pinyin\",\n" +
                "        \"search_analyzer\": \"ik_smart\"\n" +
                "      },\n" +
                "      \"image\":{\n" +
                "        \"type\": \"keyword\",\n" +
                "        \"index\": false\n" +
                "      },\n" +
                "      \"updateTime\":{\n" +
                "        \"type\": \"date\"\n" +
                "      },\n" +
                "      \"specs\":{\n" +
                "        \"type\": \"nested\",\n" +
                "        \"properties\": {\n" +
                "          \"name\":{\"type\": \"keyword\" },\n" +
                "          \"value\":{\"type\": \"keyword\" }\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}");

        int page = 1;

        while (true) {
            //分页查询spu
            PageDTO<SpuDTO> spuDTOPageDTO = this.itemClient.querySpuByPage(null, null, null, page++, 50, null);
            //如果第一条数据为空,则跳出循环
            if (spuDTOPageDTO == null || CollectionUtils.isEmpty(spuDTOPageDTO.getItems())) {
                break;
            }

            //将查询到的数据转换为goods
            List<Goods> goodsList = spuDTOPageDTO.getItems()
                    .stream()
                    .map(this::buildGoods).collect(Collectors.toList());

            //保存数据到es
            this.goodsRepository.saveAll(goodsList);

            //如果第一条有数据,但是当前页大于最大页,跳出循环
            if (page > spuDTOPageDTO.getTotalPage()) {
                break;
            }

        }


    }

    @Override
    public Goods buildGoods(SpuDTO spuDTO) {
        Goods goods = new Goods();
        BeanUtils.copyProperties(spuDTO, goods);

        //添加brandId
        goods.setBrandId(spuDTO.getBrandId());

        //添加categoryId
        goods.setCategoryId(spuDTO.getCid3());

        //添加updateTime
        goods.setUpdateTime(new Date());

        //获取sku
        List<SkuDTO> skuDTOS = this.itemClient.querySkuBySpuId(spuDTO.getId());
        //获取prices和销量
        Set<Long> prices = new HashSet();
        Long sold = 0L;
        for (SkuDTO skuDTO : skuDTOS) {
            prices.add(skuDTO.getPrice());
            sold+=skuDTO.getSold();
        }
        goods.setPrices(prices);
        goods.setSold(sold);

        if(CollectionUtils.isEmpty(skuDTOS)){
            throw new LyException(500,"Sku数据没有");
        }
        //获取图片
       goods.setImage(StringUtils.substringBefore(skuDTOS.get(0).getImages(),","));
       //获取specs
        List<SpecParamDTO> specParamDTOS = this.itemClient.queryParamsValues(spuDTO.getId(), true);
        List<Map<String,Object>> list = new ArrayList<>();
        for (SpecParamDTO specParamDTO : specParamDTOS) {
            Map<String,Object> map = new HashMap<>();
            map.put("name",specParamDTO.getName());
            map.put("value",chooseSegment(specParamDTO));
            list.add(map);
        }
        goods.setSpecs(list);

        /**
         * 自动补全的候选字段，可以包含多个值，例如分类名称、品牌名称、商品名称
         */
        //获取suggesstion

        //获取分类名称
        String brandName = StringUtils.substringBefore(spuDTO.getBrandName(),"（");
        //获取分类名称
        String categoryName = StringUtils.substringAfterLast(spuDTO.getCategoryName(), "/");

        String spuNameName = spuDTO.getName();

        String brandNameAndcategoryName  = brandName+categoryName;
        String categoryNameAndbrandName = categoryName+brandName;

        List<String> list1 = Arrays.asList(brandName, categoryName, spuNameName, brandNameAndcategoryName, categoryNameAndbrandName);

        goods.setSuggestion(list1);

        return goods;
    }

    private Object chooseSegment(SpecParamDTO p) {
        Object value = p.getValue();
        if (value == null || StringUtils.isBlank(value.toString())) {
            return "其它";
        }
        if (!p.getNumeric() || StringUtils.isBlank(p.getSegments()) || value instanceof Collection) {
            return value;
        }
        double val = parseDouble(value.toString());
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = parseDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if (segs.length == 2) {
                end = parseDouble(segs[1]);
            }
            // 判断是否在范围内
            if (val >= begin && val < end) {
                if (segs.length == 1) {
                    result = segs[0] + p.getUnit() + "以上";
                } else if (begin == 0) {
                    result = segs[1] + p.getUnit() + "以下";
                } else {
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }

    private double parseDouble(String str) {
        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
            return 0;
        }
    }
}
