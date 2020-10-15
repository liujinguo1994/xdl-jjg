package com.jjg.member.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jjg.member.model.domain.EsComrImglDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsReportVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@ApiModelProperty(required = false,value = "主键id")
	private Long id;

	/**
	 * 举报内容
	 */
	@ApiModelProperty(required = false,value = "举报内容")
	private String complaintReason;

	/**
	 * 投诉证据
	 */
	@ApiModelProperty(required = false,value = "投诉证据")
	private String intro;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(required = false,value = "创建时间")
	private Long createTime;

	/**
	 * 店铺ID
	 */
	@ApiModelProperty(required = false,value = "店铺ID")
	private Long shopId;

	/**
	 * 店铺名称
	 */
	@ApiModelProperty(required = false,value = "店铺名称")
	private String shopName;

	/**
	 * 处理状态WAIT:待处理，APPLYING:处理中，DEALT:已处理
	 */
	@ApiModelProperty(required = false,value = "WAIT:待处理，APPLYING:处理中，APPLYED:已处理")
	private String state;

	/**
	 * 联系方式
	 */
	@ApiModelProperty(required = false,value = "联系方式")
	private String phone;

	/**
	 * 会员id
	 */
	@ApiModelProperty(required = false,value = "会员id")
	private Long memberId;

	/**
	 * 会员名称
	 */
	@ApiModelProperty(required = false,value = "会员名称")
	private String memberName;
	/**
	 * 处理内容
	 */
	@ApiModelProperty(required = false,value = "处理内容")
	private String dealContent;

	private List<EsComrImglDO> comrImglDOList;
}
