package com.jjg.trade.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-11-21 10:38:46
 */
@Data
@Accessors(chain = true)
public class EsCouponTypeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

	private Long id;

	private String couponName;

	@JsonSerialize(using = ToStringSerializer.class)
	private String couponCode;

	protected Serializable pkVal() {
		return this.id;
	}

}
