package com.jjg.trade.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *  前端控制器-小程序-取消订单
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-06-17
 */
@ApiModel
@Data
public class AppletBuyerRefundForm implements Serializable{
    private static final long serialVersionUID = 758087208773569549L;
    @ApiModelProperty(name = "orderSn", value = "订单编号",required = true)
    private String orderSn;

    @ApiModelProperty(value = "SKUID",required = false)
    private List<Integer> skuId;
    @ApiModelProperty(name = "returnNum", value = "退货数量",required = false)
    private Integer returnNum;

    @ApiModelProperty(name = "refundReason", value = "退款原因",required = true)
    private String refundReason;

    @ApiModelProperty(name = "urlList", value = "图片路径",required = false)
    private List<BuyerUrlForm> urlList;

    @ApiModelProperty(name = "customerRemark", value = "客户备注",required = false)
    private String customerRemark;

    @ApiModelProperty(name = "refundType", value = "退款RETURN_MONEY/退款&退货RETURN_GOODS/换货CHANGE_GOODS/维修REPAIR_GOODS",required = true)
    private String refundType;

    @ApiModelProperty(name = "refuseType", value = "维权类型(取消订单 CANCEL_ORDER,申请售后 AFTER_SALE)",required = true)
    private String refuseType;

    @ApiModelProperty(required = true,value = "登录态标识")
    @NotBlank(message = "登录态标识不能为空")
    private String skey;

}
