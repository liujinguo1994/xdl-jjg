package com.jjg.member.model.dto;

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
 * @author WAF 826988665@qq.com
 * @since 2019-07-17 14:59:14
 */
@Data
@Accessors(chain = true)
public class EsShopLogiRelDTO implements Serializable {

    private static final long serialVersionUID = 1L;

	@JsonSerialize(using = ToStringSerializer.class)
	private Long logiId;

	@JsonSerialize(using = ToStringSerializer.class)
	private Long shopId;



}
