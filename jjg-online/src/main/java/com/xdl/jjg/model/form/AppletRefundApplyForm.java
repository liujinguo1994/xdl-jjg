package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p>
 *  前端控制器-小程序-售后相关接口
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-06-15
 */
@ApiModel
@Data
public class AppletRefundApplyForm implements Serializable{
    private static final long serialVersionUID = 758087208773569549L;

    @ApiModelProperty(name = "orderSn", value = "订单编号",required = true)
    private String orderSn;

    @ApiModelProperty(value = "SKUID",required = false)
    private Integer skuId;
    @ApiModelProperty(name = "returnNum", value = "退货数量",required = false)
    private Integer returnNum;

    @ApiModelProperty(name = "refundReason", value = "退款原因",required = true)
    private String refundReason;

    @ApiModelProperty(name = "customerRemark", value = "客户备注",required = false)
    private String customerRemark;

    @ApiModelProperty(name = "refundType", value = "退款RETURN_MONEY/退款&退货RETURN_GOODS/换货CHANGE_GOODS/维修REPAIR_GOODS",required = true)
    private String refundType;

    @ApiModelProperty(name = "refuseType", value = "维权类型(取消订单 CANCEL_ORDER,申请售后 AFTER_SALE)",required = true)
    private String refuseType;

    @ApiModelProperty(name = "url", value = "图片路径")
    private String url;

    @ApiModelProperty(required = true,value = "登录态标识")
    @NotBlank(message = "登录态标识不能为空")
    private String skey;

}
