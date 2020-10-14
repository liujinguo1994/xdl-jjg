package com.jjg.member.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * <p>
 * 
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-08-20
 */
@Data
@Accessors(chain = true)
public class EsSupplierBillDetailVO implements Serializable {

private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
									@ApiModelProperty(required = false,value = "id")
		private Integer id;

	/**
	 * 商品名称
	 */

				@ApiModelProperty(required = false,value = "goodsName")
		private String goodsName;

	/**
	 * 商品Id
	 */


				@ApiModelProperty(required = false,value = "goodsId")
		private Long goodsId;

	/**
	 * 数量
	 */
				@ApiModelProperty(required = false,value = "num")
		private Integer num;

	/**
	 * 供应商id
	 */


				@ApiModelProperty(required = false,value = "supplierId")
		private Long supplierId;

	/**
	 * 结算单id
	 */


				@ApiModelProperty(required = false,value = "billId")
		private Long billId;

	/**
	 * 状态 0 未结算
	 */
				@ApiModelProperty(required = false,value = "state")
		private Integer state;

	/**
	 * 订单编号
	 */

				@ApiModelProperty(required = false,value = "orderSn")
		private String orderSn;




protected Serializable pkVal() {
			return this.id;
		}

		}
