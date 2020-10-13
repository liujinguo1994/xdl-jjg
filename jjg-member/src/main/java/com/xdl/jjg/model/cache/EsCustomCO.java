package com.xdl.jjg.model.cache;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class EsCustomCO implements Serializable {

    private static final long serialVersionUID = 290013480449388319L;

    //分类ID
    private Long value;
    //分类名称
    private String label;
    //子分类列表
    private List<ESCustomChildrenCO> children;

}
