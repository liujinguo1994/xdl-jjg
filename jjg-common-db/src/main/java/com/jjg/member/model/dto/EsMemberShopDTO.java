package com.jjg.member.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 会员店铺关联表
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Data
@ToString
public class EsMemberShopDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 会员ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long memberId;

    /**
     * 店铺ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long shopId;

}
