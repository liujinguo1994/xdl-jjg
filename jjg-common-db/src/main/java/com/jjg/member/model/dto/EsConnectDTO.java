package com.jjg.member.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 信任登录
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Data
@ToString
public class EsConnectDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
	private Long id;

    /**
     * 会员ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long memberId;

    /**
     * 唯一标示union_id
     */
	private String unionId;

    /**
     * 信任登录类型
     */
	private String unionType;

    /**
     * 解绑时间
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long unboundTime;

	protected Serializable pkVal() {
		return this.id;
	}

}
