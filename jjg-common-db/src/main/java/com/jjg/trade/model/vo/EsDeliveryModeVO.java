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
 * 配送方式VO
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-06-04
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsDeliveryModeVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@ApiModelProperty(value = "主键ID")
	private Long id;

    /**
     * 配送方式ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "配送方式ID")
	private Long deliveryModeId;

    /**
     * 配送方式名称(自提，配送)
     */
	@ApiModelProperty(value = "配送方式名称(自提，配送)")
	private String deliveryModeName;

	protected Serializable pkVal() {
		return this.id;
	}

}
