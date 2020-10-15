package com.jjg.trade.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: SeckillGoodsVO
 * @Description: 限时购活动VO
 * @Author: libw  981087977@qq.com
 * @Date: 6/17/2019 15:03
 * @Version: 1.0
 */
@Data
@ApiModel(description = "限时购活动VO")
public class SeckillGoodsVO implements Serializable {

    @ApiModelProperty(value = "秒杀开始时间")
    private Integer seckillStartTime;

    @ApiModelProperty(value = "距离活动结束的时间，秒为单位")
    private Long distanceEndTime;

    @ApiModelProperty(value = "活动是否已经开始、1:活动正在进行中，0:未开始")
    private Integer isStart;

    @ApiModelProperty(value = "剩余可售数量")
    private Integer remainQuantity;

    @ApiModelProperty(value = "距离活动开始的时间，秒为单位。如果活动的开始时间是10点，服务器时间为8点，距离开始还有多少时间")
    private Long distanceStartTime;

    @ApiModelProperty(value = "商家ID")
    private Long shopId;

    @ApiModelProperty(value = "商品id")
    private Long goodsId;

    @ApiModelProperty(value = "商品图片")
    private String goodsImage;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品普通价格")
    private Double originalPrice;

    @ApiModelProperty(value = "是否可销售")
    private Boolean salesEnable;

    @ApiModelProperty(value = "秒杀活动价格")
    private Double seckillPrice;

    @ApiModelProperty(value = "商品规格id")
    private Long skuId;

    @ApiModelProperty(value = "已售数量")
    private Integer soldNum;

    @ApiModelProperty(value = "售空数量")
    private Integer soldQuantity;

    @ApiModelProperty(value = "不同规格下的页面url")
    private String url;
}
