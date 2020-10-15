package com.jjg.system.model.form;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class EsReFundQueryForm extends QueryPageForm {

    private static final long serialVersionUID = -7349392509774931388L;
    /**
     * 输入框输入值（维权单号,订单编号,卖家名称）
     */
    @ApiModelProperty(value = "输入框输入值（维权单号,订单编号,卖家名称）")
    private String keyword;
    /**
     * 申请时间开始
     */
    @ApiModelProperty(value = "申请时间开始")
    private Long createTimeStart;
    /**
     * 申请时间结束
     */
    @ApiModelProperty(value = "申请时间结束")
    private Long createTimeEnd;
    /**
     * 维权类型(CANCEL_ORDER:取消订单，AFTER_SALE:申请售后)
     */
    @ApiModelProperty(value = "维权类型(CANCEL_ORDER:取消订单，AFTER_SALE:申请售后)")
    private String refuseType;
    /**
     * 售后类型(RETURN_MONEY:退款,RETURN_GOODS:退货,CHANGE_GOODS:换货,REPAIR_GOODS:维修)
     */
    @ApiModelProperty(value = "售后类型(RETURN_MONEY:退款,RETURN_GOODS:退货,CHANGE_GOODS:换货,REPAIR_GOODS:维修)")
    private String refundType;
    /**
     * 处理状态(TO_BE_PROCESS:待处理,WAIT_REFUND:待退款,REFUND_FAIL:退款失败,WAIT_IN_STORAGE:待入库,WAIT_SHIP:待发货,COMPLETED:完成)
     */
    @ApiModelProperty(value = "处理状态(TO_BE_PROCESS:待处理,WAIT_REFUND:待退款,REFUND_FAIL:退款失败,WAIT_IN_STORAGE:待入库,WAIT_SHIP:待发货,COMPLETED:完成)")
    private String processStatus;
    /**
     * 审核状态(APPLY:申请中,PASS:申请通过,REFUSE:审核拒绝,CANCEL:申请取消)
     */
    @ApiModelProperty(value = "审核状态(APPLY:申请中,PASS:申请通过,REFUSE:审核拒绝,CANCEL:申请取消)")
    private String refundStatus;

}
