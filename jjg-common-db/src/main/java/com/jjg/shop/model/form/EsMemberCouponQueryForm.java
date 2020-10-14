package com.xdl.jjg.model.form;


import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Api
@Data
public class EsMemberCouponQueryForm extends QueryPageForm {

    @ApiModelProperty(value = "使用状态(1:正常，2:已使用，3失效)")
    private Long state;

    @ApiModelProperty(value = "领取时间")
    private Long createStartTime;

    @ApiModelProperty(value = "领取时间")
    private Long createEndTime;

    @ApiModelProperty(value = "优惠券主键ID")
    private Long couponId;
    @ApiModelProperty(value = "电话号码")
    private Integer mobile;

}
