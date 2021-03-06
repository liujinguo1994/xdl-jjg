package com.jjg.shop.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author WAF 826988665@qq.com
 * @since 2019-05-29
 */
@Data
@ToString
public class EsParametersDTO implements Serializable  {

    private static final long serialVersionUID = 1L;

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


}
