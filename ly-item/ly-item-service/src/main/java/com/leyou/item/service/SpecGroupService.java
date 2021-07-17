package com.leyou.item.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leyou.item.dto.SpecGroupDTO;
import com.leyou.item.dto.SpecParamDTO;
import com.leyou.item.entity.SpecGroup;
import com.leyou.item.entity.SpecParam;

import java.util.List;


public interface SpecGroupService extends IService<SpecGroup> {
    List<SpecGroupDTO> queryGroupByCategory(Integer categoryId);


    void addSpecGroup(SpecGroupDTO specGroupDTO);

    void updateSpecGroup(SpecGroupDTO specGroupDTO);

    List<SpecGroupDTO> queryGroupAndCategoryById(Integer categoryId);
}