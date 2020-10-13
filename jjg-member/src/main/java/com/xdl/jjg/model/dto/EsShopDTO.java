package com.xdl.jjg.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 店铺表
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Data
@ToString
public class EsShopDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 店铺ID
     */
	private Long id;

    /**
     * 会员ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long memberId;

    /**
     * 会员名称
     */
	private String memberName;

    /**
     * 店铺状态
     */
	private String state;

    /**
     * 店铺名称
     */
	private String shopName;

    /**
     * 店铺创建时间
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long shopCreatetime;

    /**
     * 店铺关闭时间
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long shopEndtime;

	protected Serializable pkVal() {
		return this.id;
	}

}
