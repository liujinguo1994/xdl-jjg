package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * <p>
 * 移动端-获取购物车
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2020-02-27 14:28:26
 */
@Data
@ApiModel
public class EsWapGetCartForm implements Serializable {


    private static final long serialVersionUID = 3863114069985817704L;
    /**
     * 显示方式
     */
    @ApiModelProperty(required = false,value = "显示方式",example = "all,checked")
    private String showType;


    /**
     * TAB页显示
     */
    @ApiModelProperty(required = false,value = "TAB页显示",example = "priceDown,stockShortage")
    private String tab;

    /**
     * 是否选中
     */
    @ApiModelProperty(required = false,value = "是否选中",example = "0,1")
    @Min(message = "必须为数字且,1为开启,0为关闭", value = 0)
    @Max(message = "必须为数字且,1为开启,0为关闭", value = 1)
    private Integer checked;

    /**
     * 店铺id
     */
    @ApiModelProperty(required = false,value = "店铺id")
    private  Long shopId;
}
