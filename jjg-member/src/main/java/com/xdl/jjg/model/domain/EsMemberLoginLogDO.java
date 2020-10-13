package com.xdl.jjg.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 会员登录日志
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
public class EsMemberLoginLogDO implements Serializable {


    /**
     * 登陆日志ID
     */
	private Long id;
    /**
     * 登陆用户ID
     */
	private Long memberId;
    /**
     * 用户名称
     */
	private String memberName;
    /**
     * 登陆时间
     */
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



}
