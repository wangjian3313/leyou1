package com.leyou.item.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leyou.common.exception.LyException;
import com.leyou.item.dto.SpecGroupDTO;
import com.leyou.item.dto.SpecParamDTO;
import com.leyou.item.entity.CategoryBrand;
import com.leyou.item.entity.SpecGroup;
import com.leyou.item.entity.SpecParam;
import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.service.SpecGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class SpecGroupServiceImpl extends ServiceImpl<SpecGroupMapper, SpecGroup> implements SpecGroupService {

    @Autowired
    private SpecParamServiceImpl specParamService;

    @Autowired
    private SpecGroupMapper specGroupMapper;

    @Override
    public List<SpecGroupDTO> queryGroupByCategory(Integer categoryId) {
        return SpecGroupDTO.convertEntityList(this.list(new QueryWrapper<SpecGroup>().eq("category_id",categoryId))) ;
    }

    @Override
    public void addSpecGroup(SpecGroupDTO specGroupDTO) {
        this.save(specGroupDTO.toEntity(SpecGroup.class));
    }

    @Override
    public void updateSpecGroup(SpecGroupDTO specGroupDTO) {
        this.updateById(specGroupDTO.toEntity(SpecGroup.class));
    }

    @Override
    public List<SpecGroupDTO> queryGroupAndCategoryById(Integer categoryId) {
        //方式一
      /*  List<SpecGroup> specGroupList = this.list(new QueryWrapper<SpecGroup>().eq("category_id", categoryId));
        List<SpecGroupDTO> specGroupDTOList = SpecGroupDTO.convertEntityList(specGroupList);

        for (SpecGroupDTO specGroupDTO : specGroupDTOList) {
            List<SpecParam> specParamList = this.specParamService.list(new QueryWrapper<SpecParam>().eq("group_id", specGroupDTO.getId()));
            List<SpecParamDTO> specParamDTOList = SpecParamDTO.convertEntityList(specParamList);
            specGroupDTO.setParams(specParamDTOList);
        }*/

       //方式二
        List<SpecGroup> specGroupList = this.list(new QueryWrapper<SpecGroup>().eq("category_id", categoryId));
        List<SpecGroupDTO> specGroupDTOList = SpecGroupDTO.convertEntityList(specGroupList);
        List<SpecParamDTO> paramList = this.specParamService.querySpecParams(categoryId,null,null);
        Map<Long, List<SpecParamDTO>> map = paramList.stream().collect(Collectors.groupingBy(SpecParamDTO::getGroupId));
        // 把参数放入group
        for (SpecGroupDTO groupDTO : specGroupDTOList) {
            groupDTO.setParams(map.get(groupDTO.getId()));
        }

      //方式三

    //    List<SpecGroupDTO> specGroupDTOList = this.specGroupMapper.queryGroupAndCategoryById(categoryId);
        return specGroupDTOList;
    }

}