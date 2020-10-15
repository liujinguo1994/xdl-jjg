package com.jjg.trade.model.form;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("获取时间点内秒杀商品Form")
public class EsSeckillTimelineGoodsForm extends QueryPageForm {
    private static final long serialVersionUID = 1L;
    @NotBlank
    @ApiModelProperty(value = "日期",example = "2020-06-11",required = true)
    private String day;
    @NotNull
    @ApiModelProperty(value = "时间点",example = "15",required = true)
    private Integer timeline;
}
