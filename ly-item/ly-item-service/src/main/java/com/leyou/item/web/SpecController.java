package com.leyou.item.web;

import com.leyou.item.dto.SpecGroupDTO;
import com.leyou.item.dto.SpecParamDTO;
import com.leyou.item.service.SpecGroupService;
import com.leyou.item.service.SpecParamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/spec")
public class SpecController {

    private SpecGroupService groupService;
    private SpecParamService paramService;

    public SpecController(SpecGroupService groupService, SpecParamService paramService) {
        this.groupService = groupService;
        this.paramService = paramService;
    }

    /**
     *根据商品分类id，查询规格组的集合
     */
    @GetMapping("/groups/of/category")
    public ResponseEntity<List<SpecGroupDTO>> queryGroupByCategory(@RequestParam("id") Integer categoryId){
        return ResponseEntity.ok(this.groupService.queryGroupByCategory(categoryId));
    }
    /**
     * 根据条件查询规格参数集合
     */
    @GetMapping("/params")
    public ResponseEntity<List<SpecParamDTO>> querySpecParams(
            @RequestParam(value = "categoryId",required = false) Integer categoryId,
            @RequestParam(value = "groupId",required = false) Integer groupId,
            @RequestParam(value = "searching",required = false) Boolean searching
    ){
        return ResponseEntity.ok(this.paramService.querySpecParams(categoryId,groupId,searching));
    }

    /**
     * 新增规格租
     */
    @PostMapping("/group")
    @Transactional
    public ResponseEntity<Void> addSpecGroup(@RequestBody SpecGroupDTO specGroupDTO){
        this.groupService.addSpecGroup(specGroupDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    /**
     * 修改规格组
     */
    @PutMapping("/group")
    @Transactional
    public ResponseEntity<Void> updateSpecGroup(@RequestBody SpecGroupDTO specGroupDTO){
        this.groupService.updateSpecGroup(specGroupDTO);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 新增规格参数
     */
    @PostMapping("/param")
    @Transactional
    public ResponseEntity<Void> addSpecParam(@RequestBody SpecParamDTO specParamDTO){
        this.paramService.addSpecParam(specParamDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    /**
     * 修改规格参数
     */
    @PutMapping("/param")
    @Transactional
    public ResponseEntity<Void> updateSpecParam(@RequestBody SpecParamDTO specParamDTO){
        this.paramService.updateSpecParam(specParamDTO);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    /**
     * 根据分类id查询规格组及组内参数
     */
    @GetMapping("/list")
    public ResponseEntity<List<SpecGroupDTO>> queryGroupAndCategoryById(@RequestParam("id") Integer categoryId){
        return ResponseEntity.ok(this.groupService.queryGroupAndCategoryById(categoryId));
    }


}