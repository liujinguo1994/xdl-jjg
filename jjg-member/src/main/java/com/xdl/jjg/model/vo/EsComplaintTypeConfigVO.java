package com.xdl.jjg.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 投诉原因配置VO
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-07-23 09:56:28
 */
@Data
@ApiModel
public class EsComplaintTypeConfigVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@ApiModelProperty(required = false,value = "主键ID",example = "1")
	private Long id;

    /**
     * 投诉类型
     */
	@ApiModelProperty(value = "投诉类型")
	private String complainType;

    /**
     * 创建时间
     */
	@ApiModelProperty(required = false,value = "创建时间",example = "123456")
	private Long createTime;


}
