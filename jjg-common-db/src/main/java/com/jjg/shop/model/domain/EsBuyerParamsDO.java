package com.jjg.shop.model.domain;

/**
 * @author wangaf
 * @date 2019/11/2 16:29
 **/

import lombok.Data;

import java.io.Serializable;

/**
 @Author wangaf 826988665@qq.com
 @Date 2019/11/2
 @Version V1.0
 **/
@Data
public class EsBuyerParamsDO implements Serializable {


    /**
     * 主键ID
     */
    private Long id;
    /**
     * 参数名称
     */
    private String paramName;
    /**
     * 参数类型1 输入项   2 选择项
     */
    private Integer paramType;
    /**
     * 选择值，当参数类型是选择项2时，必填，逗号分隔
     */
    private String options;
    /**
     * 是否可索引，0 不显示 1 显示
     */
    private Integer isIndex;
    /**
     * 是否必填是  1    否   0
     */
    private Integer required;
    /**
     * 参数分组id
     */
    private Long groupId;
    /**
     * 商品分类id
     */
    private Long categoryId;
    /**
     * 排序
     */
    private Integer sort;

    /**
     * 参数值
     */
    private String paramValue;

}
