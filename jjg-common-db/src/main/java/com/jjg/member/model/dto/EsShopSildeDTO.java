package com.jjg.member.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 店铺幻灯片
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Data
@ToString
public class EsShopSildeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 幻灯片Id
     */
	private Long id;

    /**
     * 店铺Id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long shopId;

    /**
     * 幻灯片URL
     */
	private String sildeUrl;

    /**
     * 图片URL
     */
	private String img;

	protected Serializable pkVal() {
		return this.id;
	}

}
