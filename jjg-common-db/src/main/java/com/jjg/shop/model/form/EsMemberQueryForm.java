package com.xdl.jjg.model.form;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@Api
public class EsMemberQueryForm extends QueryPageForm {

    /**
     * 会员类别
     */
    @ApiModelProperty(value = "会员类别 0 新增会员 1活跃会员 2 休眠会员 3 普通会员 4 潜在用户")
    private Integer memberType;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private Long beginTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private Long endTime;

    /**
     * 输入值
     */
    @ApiModelProperty(value = "输入值")
    private String coent;

}
