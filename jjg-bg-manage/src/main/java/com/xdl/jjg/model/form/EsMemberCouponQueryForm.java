package com.xdl.jjg.model.form;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 会员优惠券
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-04
 */
@Data
@ApiModel
public class EsMemberCouponQueryForm extends QueryPageForm {


    private static final long serialVersionUID = -7256020948191378135L;

    /**
     * 会员id
     */
    @ApiModelProperty(required = true,value = "会员id",example = "1")
    @NotNull(message = "会员id不能为空")
    private Long memberId;

    /**
     * 输入框输入值(优惠券编号，名称，关联订单号)
     */
    @ApiModelProperty(value = "输入框输入值(优惠券编号，名称，关联订单号)")
    private String keyword;

    /**
     * 商家ID
     */
    @ApiModelProperty(value = "商家ID",example = "1")
    private Long shopId;

    /**
     * 使用状态(1未使用，2已使用)
     */
    @ApiModelProperty(value = "使用状态(1未使用，2已使用)", example = "1")
    private Integer state;

    /**
     * 是否过期(1未过期，2已过期)
     */
    @ApiModelProperty(value = "是否过期(1未过期，2已过期)",example = "1")
    private Integer overdueState;

}
