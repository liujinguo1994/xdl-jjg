package com.jjg.shop.model.domain;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ParameterGroupDO implements Serializable {
    //参数组名称
    private String groupName;
    //参数组ID
    private Long groupId;
   //参数组关联的参数集合
    private List<com.xdl.jjg.model.domain.EsParametersDO>  params;
}
