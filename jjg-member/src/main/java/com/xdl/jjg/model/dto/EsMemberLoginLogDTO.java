package com.xdl.jjg.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 会员登录日志
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Data
@ToString
public class EsMemberLoginLogDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 登陆日志ID
     */
	private Long id;

    /**
     * 登陆用户ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long memberId;

    /**
     * 用户名称
     */
	private String memberName;

    /**
     * 登陆时间
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long loginTime;

    /**
     * 登陆IP
     */
	private String loginIp;

    /**
     * 登陆类型：0 app,1 pc ,2 h5
     */
	private Integer loginType;

    /**
     * 登录状态
     */
	private Integer state;

    /**
     * 失败原因
     */
	private String failReason;

	protected Serializable pkVal() {
		return this.id;
	}

}
