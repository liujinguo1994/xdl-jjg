package com.jjg.member.model.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 投诉原因配置
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-07-23 16:07:57
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EsComplaintReasonConfigDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
	private Long id;

    /**
     * 投诉原因
     */
	private String complaintReason;

    /**
     * 投诉类型id
     */
	private Long complainTypeId;
	/**
	 * 投诉类型名称
	 */
	private String complaintTypeName;

    /**
     * 创建时间
     */
	private Long createTime;

}
