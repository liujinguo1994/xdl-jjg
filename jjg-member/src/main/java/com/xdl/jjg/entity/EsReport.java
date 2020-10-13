package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * <p>
 * 举报表
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-07-25 10:07:50
 */
@Data
@TableName("es_report")
public class EsReport extends Model<EsReport> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;

	/**
	 * 举报内容
	 */
	@TableField("complaint_reason")
	private String complaintReason;

	/**
	 * 投诉证据
	 */
	@TableField("intro")
	private String intro;


	/**
	 * 店铺ID
	 */
	@TableField("shop_id")
	private Long shopId;

	/**
	 * 店铺名称
	 */
	@TableField("shop_name")
	private String shopName;

	/**
	 * 处理状态(WAIT:待处理，APPLYING:处理中，APPLYED:已处理)
	 */
	@TableField("state")
	private String state;

	/**
	 * 联系方式
	 */
	@TableField("phone")
	private String phone;

	/**
	 * 会员id
	 */
	@TableField("member_id")
	private Long memberId;

	/**
	 * 会员名称
	 */
	@TableField("member_name")
	private String memberName;

	/**
	 * 创建时间
	 */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Long createTime;

	/**
	 * 是否有效
	 */
	@TableLogic(value = "0", delval = "1")
	@TableField("is_del")
	private Integer isDel=0;

	/**
	 * 处理内容
	 */
	@TableField(value = "deal_content")
	private String dealContent;

}
