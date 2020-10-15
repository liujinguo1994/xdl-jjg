package com.jjg.trade.model.form.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 移动端-购物车设置活动
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2020-05-06 13:28:26
 */
@Data
@ApiModel
public class EsWapCartSetPromotion implements Serializable {


    private static final long serialVersionUID = 3863114069985817704L;
    /**
     * 活动ID
     */
    @ApiModelProperty(required = false,value = "活动ID",example = "1")
    @NotNull(message = "活动ID必填")
    private Long activityId;


    /**
     * skuId
     */
    @ApiModelProperty(required = false,value = "skuId")
    @NotNull(message = "skuId必填")
    private Long skuId;

}
