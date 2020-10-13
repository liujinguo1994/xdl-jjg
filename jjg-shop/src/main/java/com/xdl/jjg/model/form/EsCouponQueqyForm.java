package com.xdl.jjg.model.form;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Api
@Data
public class EsCouponQueqyForm extends QueryPageForm {

    @ApiModelProperty(value = "优惠券类型")
    private String couponType;

    @ApiModelProperty(value = "优惠券名称")
    private String title;
    @ApiModelProperty(value = "优惠券状态 0 表示 未开始，1 进行中，2 已结束")
    private Integer couponStatus;
}
