package com.xdl.jjg.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 信任登录
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
public class EsConnectDO implements Serializable {


    /**
     * id
     */
	private Long id;
    /**
     * 会员ID
     */
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
	private Long unboundTime;


}
