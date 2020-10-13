package com.xdl.jjg.model.vo;/**
 * @author wangaf
 * @date 2019/11/2 16:29
 **/

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 @Author wangaf 826988665@qq.com
 @Date 2019/11/2
 @Version V1.0
 **/
@Data
@ApiModel
public class EsBuyerParamsVO implements Serializable {


    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    private Long id;
    /**
     * 参数名称
     */
    @ApiModelProperty(value = "参数名称")
    private String paramName;
    /**
     * 参数类型1 输入项   2 选择项
     */
    @ApiModelProperty(value = "参数类型1 输入项   2 选择项")
    private Integer paramType;
    /**
     * 选择值，当参数类型是选择项2时，必填，逗号分隔
     */
    @ApiModelProperty(value = "选择值，当参数类型是选择项2时，必填，逗号分隔")
    private String options;
    /**
     * 是否可索引，0 不显示 1 显示
     */
    @ApiModelProperty(value = "是否可索引，0 不显示 1 显示")
    private Integer isIndex;
    /**
     * 是否必填是  1    否   0
     */
    @ApiModelProperty(value = "是否必填是  1    否   0")
    private Integer required;
    /**
     * 参数分组id
     */
    @ApiModelProperty(value = "参数分组id")
    private Long groupId;
    /**
     * 商品分类id
     */
    @ApiModelProperty(value = "商品分类id")
    private Long categoryId;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;

    /**
     * 参数值
     */
    @ApiModelProperty(value = "参数值")
    private String paramValue;

}
