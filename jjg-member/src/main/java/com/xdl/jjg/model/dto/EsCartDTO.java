package com.xdl.jjg.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 购物车
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Data
@ToString
public class EsCartDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;

    /**
     * 会员ID
     */
	private Long memberId;

    /**
     * 创建时间
     */
	private Long createTime;

    /**
     * 修改时间
     */
	private Long updateTime;

    /**
     * 提交时间
     */
	private Long submitTime;

    /**
     * 提交IP
     */
	private String submitIp;

    /**
     * 备注
     */
	private String remake;

    /**
     * 手机MEI
     */
	private String phoneImei;

    /**
     * 手机MAC
     */
	private String phoneMac;

	protected Serializable pkVal() {
		return this.id;
	}

}
