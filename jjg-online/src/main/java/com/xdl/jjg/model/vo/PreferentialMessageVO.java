package com.xdl.jjg.model.vo;/**
 * Description: zhuox-shop-trade
 * <p>
 * Created by Administrator on 2020/4/24 13:37
 */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName PreferentialMessageVO
 * @Description: TODO
 * @Author Administrator
 * @Date 2020/4/24 
 * @Version V1.0
 **/
@Data
@ApiModel(description = "优惠信息")
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PreferentialMessageVO implements Serializable {

    @ApiModelProperty(value = "促销活动工具类型")
    private String promotionType;
    @ApiModelProperty(value = "商品集合")
    private List<CartItemsVO> skuList;
    @ApiModelProperty(value = "优惠类型 用于满减活动(不满足优惠：Non,满减：FullMinus，满折:FullDiscount,满免邮：FullFreeShip，满赠品：FullSendGift,满赠券：FullSendBonus)")
    private String preferentialType;
    @ApiModelProperty(value = "优惠类型图标高亮（1，高亮；2暗淡）")
    private Integer preferentialPic;
    @ApiModelProperty(value = "选中满减活动的件数")
    private Integer checkNum;
    @ApiModelProperty(value = "参加优惠商品总价")
    private Double preferentialTotal;
    @ApiModelProperty(value = "优惠满足门槛条件")
    private Double preferentialThreshold;
    @ApiModelProperty(value = "差额（还差多少钱满足优惠 去凑单）")
    private Double difference;
    @ApiModelProperty(value = "优惠价格")
    private Double preferentialPrice;
    @ApiModelProperty(value = "优惠折扣")
    private Double preferentialDiscount;
    @ApiModelProperty(value = "满赠品信息")
    private EsFullDiscountGiftVO esSendDiscountGift;
    @ApiModelProperty(value = "满赠券信息")
    private EsCouponVO esSendCoupon;

    public PreferentialMessageVO(){
        // V_2 start
        this.preferentialPrice = 0.0;
        this.preferentialPic = 2;
        this.checkNum = 0;
        // V_2 end
    }

}
