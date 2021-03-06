package com.leyou.item.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.leyou.common.dto.BaseDTO;
import com.leyou.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.print.DocFlavor;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SpecGroupDTO extends BaseDTO {
    @TableId(type = IdType.INPUT)
    private Long id;

    private Long categoryId;

    private String name;
    private List<SpecParamDTO> params;


    public SpecGroupDTO(BaseEntity entity) {
        super(entity);
    }

    public static <T extends BaseEntity> List<SpecGroupDTO> convertEntityList(Collection<T> list) {
        if(list == null){
			return Collections.emptyList();
		}
        return list.stream().map(SpecGroupDTO::new).collect(Collectors.toList());
    }
}