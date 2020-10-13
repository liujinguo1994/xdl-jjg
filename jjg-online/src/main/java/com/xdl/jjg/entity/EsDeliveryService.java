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
 * 自提点信息维护
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-05-31
 */
@Data
@Accessors(chain = true)
@TableName("es_delivery_service")
public class EsDeliveryService extends Model<EsDeliveryService> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
	/**
	 * 自提点名称
	 */
	@TableField("delivery_name")
	private String deliveryName;
	/**
	 * 有效状态
	 */
	@TableField("state")
	private Integer state;

	/**
	 * 门店地址
	 */
	@TableField("address")
	private String address;

	/**
	 * 联系电话
	 */
	@TableField("phone")
	private String phone;
	/**
	 * 签约公司ID
	 */
	@TableField("company_id")
	private Long companyId;
	/**
	 * 签约公司名称
	 */
	@TableField("company_name")
	private String companyName;
	/**
	 * 签约公司code
	 */
	@TableField("company_code")
	private String companyCode;

	/**
	 * 店铺ID 因为自提点服务是 针对自营的 不需要关联shopId 数据库已经删除
	 */
//	@TableField("shop_id")
//	private Long shopId;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
