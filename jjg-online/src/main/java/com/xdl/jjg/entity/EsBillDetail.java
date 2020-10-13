package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 结算单
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-05 15:33:45
 */
@Data
@Accessors(chain = true)
@TableName("es_bill_detail")
public class EsBillDetail extends Model<EsBillDetail> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;

	/**
	 * 订单编号
	 */
	@TableField("order_sn")
	private String orderSn;

	/**
	 * 结算状态 0 已结算 1 未结算
	 */
	@TableField("state")
	private Integer state;

	/**
	 * 类型(0 供应商 1 签约公司)
	 */
	@TableField("type")
	private Integer type;

	/**
	 * 签约公司结算单ID
	 */
	@TableField("sign_company_id")
	private Long signCompanyId;

	/**
	 * 店铺结算ID
	 */
	@TableField("shop_bill_id")
	private Long shopBillId;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
