package com.jjg.member.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 售后维护配置VO
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-06-04
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsAfterSaleVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键 ID
     */
	@ApiModelProperty(value = "主键 ID")
	private Long id;

    /**
     * 退货时间
     */
	@ApiModelProperty(value = "退货时间")
	private Integer returnGoodsTime;

    /**
     * 换货时间
     */
	@ApiModelProperty(value = "换货时间")
	private Integer changeGoodsTime;

    /**
     * 商品分类ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "商品分类ID")
	private Long categoryId;

	protected Serializable pkVal() {
		return this.id;
	}

}
