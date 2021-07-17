package com.leyou.item.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leyou.item.dto.SpecGroupDTO;
import com.leyou.item.entity.SpecGroup;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description:
 * @Author: Qi
 * @Date: 15:06 2021/7/16
 */
@Repository
public interface SpecGroupMapper extends BaseMapper<SpecGroup> {

    List<SpecGroupDTO> queryGroupAndCategoryById(@Param("categoryId") Integer categoryId);
}
