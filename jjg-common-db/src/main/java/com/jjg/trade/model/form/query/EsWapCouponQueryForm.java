package com.jjg.trade.model.form.query;


import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 优惠券QueryForm
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-06-04
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsWapCouponQueryForm extends QueryPageForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 优惠券名称
     */
    @ApiModelProperty(value = "优惠券名称")
    private String title;

    /**
     * 优惠券面额
     */
    @ApiModelProperty(value = "优惠券面额")
    private Double couponMoney;

    /**
     * 优惠券门槛价格
     */
    @ApiModelProperty(value = "优惠券门槛价格")
    private Double couponThresholdPrice;


    /**
     * 店铺ID
     */
    @ApiModelProperty(value = "店铺ID")
    private Long shopId;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String sellerName;


}
