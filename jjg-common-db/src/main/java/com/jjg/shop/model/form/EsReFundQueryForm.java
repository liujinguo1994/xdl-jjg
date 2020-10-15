package com.jjg.shop.model.form;
import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class EsReFundQueryForm extends QueryPageForm {

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private Long createTimeStart;
    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private Long createTimeEnd;

    /**
     * 维权单号
     */
    @ApiModelProperty(value = "维权单号")
    private String sn;
    /**
     * 关联订单号
     */
    @ApiModelProperty(value = "关联订单号")
    private String orderSn;

    /**
     * 维权类型
     */
    @ApiModelProperty(value = "维权类型")
    private String refuseType;
    /**
     * 售后类型
     */
    @ApiModelProperty(value = "售后类型")
    private String refundType;

    /**
     * 售后状态
     */
    @ApiModelProperty(value = "售后状态")
    private String refundStatus;
}
