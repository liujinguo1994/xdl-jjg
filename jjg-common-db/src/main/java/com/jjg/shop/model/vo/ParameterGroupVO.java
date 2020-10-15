package com.jjg.shop.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class ParameterGroupVO {
    //参数组名称
    private String groupName;
    //参数组ID
    private Long groupId;
   //参数组关联的参数集合
    private List<EsParametersVO>  params;
}
