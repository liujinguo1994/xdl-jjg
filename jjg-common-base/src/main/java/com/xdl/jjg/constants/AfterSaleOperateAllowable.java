package com.xdl.jjg.constants;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * @Description 操作被允许的情况
 * @ClassName AfterSaleOperateAllowable
 * @author zjp
 * @version v7.0
 * @since v7.0 上午11:32 2018/5/8
 */
@Data
public class AfterSaleOperateAllowable implements Serializable{
	private static final long serialVersionUID = -6083914452276811925L;

	public AfterSaleOperateAllowable(){}

	@ApiModelProperty(value = "是否允许被取消" ,name = "allow_cancel")
	private boolean allowCancel;

	@ApiModelProperty(value = "是否允许申请" ,name = "allow_apply")
	private boolean allowApply;

	@ApiModelProperty(value = "是否允许退货入库" ,name = "allow_stock_in")
	private boolean allowStockIn;

	@ApiModelProperty(value = "是否允许商家审核" ,name = "allow_seller_approval")
	private boolean allowSellerApproval;

	@ApiModelProperty(value = "是否允许商家退款" ,name = "allow_seller_refund")
	private boolean allowSellerRefund;
	@ApiModelProperty(value = "是否允许商家发货" ,name = "allow_seller_refund")
	private boolean allowSellerDelivery;
	@Override
	public String toString() {
		return "AfterSaleOperateAllowable{" +
				"allowCancel=" + allowCancel +
				", allowApply=" + allowApply +
				", allowStockIn=" + allowStockIn +
				", allowSellerApproval=" + allowSellerApproval +
				", allowSellerRefund=" + allowSellerRefund +
				'}';
	}
}
