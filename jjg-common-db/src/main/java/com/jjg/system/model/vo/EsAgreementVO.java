package com.jjg.system.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * <p>
 * 协议维护
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel
public class EsAgreementVO implements Serializable {
    private static final long serialVersionUID = -1921759492847034290L;
    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID", example = "1")
    private Long id;
    /**
     * 协议编号
     */
    @ApiModelProperty(value = "协议编号")
    private String agrNo;
    /**
     * 协议内容
     */
    @ApiModelProperty(value = "协议内容")
    private String content;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", example = "1")
    private Long createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间", example = "1")
    private Long updateTime;
    /**
     * 协议名称
     */
    @ApiModelProperty(value = "协议名称")
    private String agrName;
    /**
     * 协议版本
     */
    @ApiModelProperty(value = "协议版本")
    private String version;

}
