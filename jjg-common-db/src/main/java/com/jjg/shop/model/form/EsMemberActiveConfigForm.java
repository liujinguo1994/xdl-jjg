package com.jjg.shop.model.form;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@Api
public class EsMemberActiveConfigForm implements Serializable {

    @ApiModelProperty(value = "会员类别 0 新增会员 1活跃会员 2 休眠会员 3 普通会员 4 潜在会员")
    private Integer memberType;

    @ApiModelProperty(value = "天数")
    private Integer days;

    @ApiModelProperty(value = "订单数")
    private Integer orders;

    @ApiModelProperty(value = "连续天数")
    private Integer visitDays;
}
