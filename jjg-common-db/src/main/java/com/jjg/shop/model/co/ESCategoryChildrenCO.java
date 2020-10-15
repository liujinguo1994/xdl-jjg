package com.jjg.shop.model.co;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ESCategoryChildrenCO implements Serializable {
    //分类ID
    private Long value;
    //分类名称
    private String label;
    //子分类列表
    private List<ESCategoryChildrenCO> childrenDO;
}
