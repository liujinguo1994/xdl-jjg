package com.xdl.jjg.model.cache;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ESCustomChildrenCO implements Serializable {

    private static final long serialVersionUID = -2638990498320308483L;
    //分类ID
    private Long value;
    //分类名称
    private String label;
    //子分类列表
    private List<ESCustomChildrenCO> children;

}
