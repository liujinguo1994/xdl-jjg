package com.jjg.shop.model.co;

import com.jjg.shop.model.co.ESCategoryChildrenCO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class EsCategoryCO implements Serializable {
    //分类ID
    private Long value;
    //分类名称
    private String label;
    //子分类列表
    private List<ESCategoryChildrenCO> childrenDO;
}
