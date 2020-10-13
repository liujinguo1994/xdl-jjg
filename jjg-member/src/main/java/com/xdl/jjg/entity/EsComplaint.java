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
 * @since 2019-07-26 11:07:50
 */
@Data
@TableName("es_complaint")
public class EsComplaint extends Model<EsComplaint> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;

	/**
	 * 投诉内容
	 */
	@TableField("content")
	private String content;

	/**
	 * 投诉原因
	 */
	@TableField("reason_id")
	private Long reasonId;

	/**
	 * 子订单订单编号
	 */
	@TableField("order_sn")
	private String orderSn;

	/**
	 * 商品id
	 */
	@TableField("good_id")
	private Long goodId;
	/**
	 * 店铺ID
	 */
	@TableField("shop_id")
	private Long shopId;

	/**
	 * 投诉类型
	 */
	@TableField("type_id")
	private Long typeId;

	/**
	 * 处理状态
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
	 * 是否撤销， 0不撤销， 1撤销
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
