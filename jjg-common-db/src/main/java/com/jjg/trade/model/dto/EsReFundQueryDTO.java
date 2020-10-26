package com.jjg.trade.model.dto;

import com.shopx.common.model.result.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * @Description: 系统后台售后单查询DTO
 * @Author       LiuJG 344009799@qq.com
 * @Date         2019/8/12 10:00
 *
 */
@ApiModel
@Data
public class EsReFundQueryDTO extends QueryPageForm {

    private static final long serialVersionUID = -7349392509774931388L;
    /**
     * 输入框输入值（维权单号,订单编号,卖家名称）
     */
    @ApiModelProperty(value = "输入框输入值（维权单号,订单编号,卖家名称）,")
    private String keyword;

    /**
     * 买家端 售后列表 会员ID
     */
    @ApiModelProperty(value = "会员ID")
    private Long memberId;
    /**
     * 买家端 查询时间段
     */
    @ApiModelProperty(value = "查询时间段")
    private Integer time;

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
     * 处理状态
     */
    @ApiModelProperty(value = "处理状态")
    private String processStatus;
    /**
     * 审核状态
     */
    @ApiModelProperty(value = "审核状态")
    private String refundStatus;

    // 卖家端
    /**
     * 卖家id
     */
    private Long shopId;
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

}
