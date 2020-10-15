package com.jjg.trade.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 团购分类VO
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-06-04
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsGroupBuyCatVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分类id
     */
	@ApiModelProperty(value = "分类id")
	private Long id;

    /**
     * 父类id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "父类id")
	private Long parentId;

    /**
     * 分类名称
     */
	@ApiModelProperty(value = "分类名称")
	private String catName;

    /**
     * 分类结构目录
     */
	@ApiModelProperty(value = "分类结构目录")
	private String catPath;

    /**
     * 分类排序
     */
	@ApiModelProperty(value = "分类排序")
	private Integer catOrder;

	protected Serializable pkVal() {
		return this.id;
	}

}
