package com.xdl.jjg.model.dto;/**
 * Description: zhuox-shop-trade
 * <p>
 * Created by Administrator on 2019/10/15 14:54
 */

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName DeliveryMessageForm
 * @Description: TODO
 * @Author Administrator
 * @Date 2019/10/15 
 * @Version V1.0
 **/
@Data
@ApiModel
public class EsDeliveryMessageDTO implements Serializable {
    @ApiModelProperty(value = "自提点ID")
    private Long DeliveryId;


    @ApiModelProperty(value = "自提点日期ID")
    private Long DateId;


    @ApiModelProperty(value = "自提时间点ID")
    private Long TimeId;

    /**
     * 自提点名称
     */
    @ApiModelProperty(value = "自提点名称")
    private String deliveryName;

    /**
     * 门店地址
     */
    @ApiModelProperty(value = "门店地址")
    private String address;

    /**
     * 自提日期
     */
    @ApiModelProperty(value = "自提日期")
    private Long selfDate;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private Long startTime;
    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private Long endTime;

    @ApiModelProperty(value = "收货人姓名")
    private String receiverName;

    @ApiModelProperty(value = "收货人手机")
    private String receiverMobile;
}
