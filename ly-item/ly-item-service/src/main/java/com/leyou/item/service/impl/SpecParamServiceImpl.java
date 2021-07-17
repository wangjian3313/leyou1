package com.leyou.item.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leyou.common.exception.LyException;
import com.leyou.item.dto.SpecParamDTO;
import com.leyou.item.entity.SpecParam;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.service.SpecParamService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SpecParamServiceImpl extends ServiceImpl<SpecParamMapper, SpecParam> implements SpecParamService {
    @Override
    public List<SpecParamDTO> querySpecParams(Integer categoryId, Integer groupId, Boolean searching) {
        List<SpecParam> paramList = this.list(new QueryWrapper<SpecParam>()
                .eq(categoryId != null, "category_id", categoryId)
                .eq(groupId != null, "group_id", groupId)
                .eq(searching != null, "searching", searching));
        return SpecParamDTO.convertEntityList(paramList);
    }

    @Override
    public void addSpecParam(SpecParamDTO specParamDTO) {
        this.save(specParamDTO.toEntity(SpecParam.class));
    }

    @Override
    public void updateSpecParam(SpecParamDTO specParamDTO) {
        this.updateById(specParamDTO.toEntity(SpecParam.class));
    }

}