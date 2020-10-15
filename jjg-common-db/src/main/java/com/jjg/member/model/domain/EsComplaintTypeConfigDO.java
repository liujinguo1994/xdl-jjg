package com.jjg.member.model.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 投诉类型
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-07-23 16:07:57
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EsComplaintTypeConfigDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;

    /**
     * 投诉类型
     */
	private String complainType;

    /**
     * 创建时间
     */
	private Long createTime;

}
