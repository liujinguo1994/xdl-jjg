package com.xdl.jjg.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 举报表
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-07-25 10:49:10
 */
@Data
@ToString
public class EsReportDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	private Long id;

	/**
	 * 举报内容
	 */
	private String complaintReason;

	/**
	 * 投诉证据
	 */
	private String intro;

	/**
	 * 创建时间
	 */
	private Long createTime;

	/**
	 * 店铺ID
	 */
	private Long shopId;

	/**
	 * 店铺名称
	 */
	private String shopName;

	/**
	 * 处理状态(WAIT:待处理，APPLYING:处理中，APPLYED:已处理)
	 */
	private String state;

	/**
	 * 联系方式
	 */
	private String phone;

	/**
	 * 会员id
	 */
	private Long memberId;

	/**
	 * 会员名称
	 */
	private String memberName;

	/**
	 * 处理内容
	 */
	private String dealContent;

	private List<EsComrImglDTO> comrImglDTOList;
}
