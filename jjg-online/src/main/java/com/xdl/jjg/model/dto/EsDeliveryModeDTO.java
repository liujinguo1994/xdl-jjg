package com.xdl.jjg.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 配送方式
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-05-31
 */
@Data
@Accessors(chain = true)
public class EsDeliveryModeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;

    /**
     * 配送方式ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long deliveryModeId;

    /**
     * 配送方式名称(自提，配送)
     */
	private String deliveryModeName;

	protected Serializable pkVal() {
		return this.id;
	}

}
