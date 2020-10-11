package com.xdl.jjg.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 售后申请原因
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-12-16
 */
@Data
public class EsReturnReasonDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
	private Long id;

    /**
     * 原因
     */
	private String reason;

    /**
     * 售后类型
     */
	private String refundType;

    /**
     * 创建时间
     */
	private Long createTime;
}
