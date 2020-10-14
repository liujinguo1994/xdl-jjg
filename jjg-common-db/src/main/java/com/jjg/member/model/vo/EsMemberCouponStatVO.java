package com.jjg.member.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * app端优惠券数量统计
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel
public class EsMemberCouponStatVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 可使用优惠券数量
     */
    @ApiModelProperty(value = "可使用优惠券数量")
    private Integer enabledCount;
    /**
     * 已使用优惠券数量
     */
    @ApiModelProperty(value = "已使用优惠券数量")
    private Integer usedCount;
    /**
     * 失效优惠券数量
     */
    @ApiModelProperty(value = "失效优惠券数量")
    private Integer failureCount;

}
