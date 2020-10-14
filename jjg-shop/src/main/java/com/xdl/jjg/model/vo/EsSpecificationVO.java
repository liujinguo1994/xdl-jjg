package com.jjg.member.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * VO
 * </p>
 *
 * @author WNAGAF 826988665@qq.com
 * @since 2019-06-05 17:30:01
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsSpecificationVO implements Serializable {
    /**
     * 主键ID
     */
	@ApiModelProperty(value = "主键ID")
	private Long id;

    /**
     * 规格项名称
     */
	@ApiModelProperty(value = "规格项名称")
	private String specName;

    /**
     * 是否被删除0未 删除   1  删除
     */
	@ApiModelProperty(value = "是否被删除0未 删除   1  删除")
	private Integer isDel;

    /**
     * 规格描述
     */
	@ApiModelProperty(value = "规格描述")
	private String specMemo;

    /**
     * 所属卖家 0属于平台
     */
	@ApiModelProperty(value = "所属卖家 0属于平台")
	private Long shopId;

}
