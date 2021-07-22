package com.leyou.search.client;

import com.leyou.common.dto.PageDTO;
import com.leyou.item.dto.BrandDTO;
import com.leyou.item.dto.SpuDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ItemClientTest {

    @Autowired
    private ItemClient itemClient;

    @Test
    public void querySpuByPage() {
        PageDTO<SpuDTO> pageDTO = this.itemClient.querySpuByPage(null, null, 2, 1, 5, null);
        List<SpuDTO> items = pageDTO.getItems();
        System.out.println(items);
        while (true){

        }
    }

}