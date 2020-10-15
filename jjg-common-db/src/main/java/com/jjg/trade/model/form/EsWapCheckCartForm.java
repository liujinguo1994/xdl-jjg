package com.jjg.trade.model.form;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 移动端-选中购物车
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2020-02-27 13:50:26
 */
@Data
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class EsWapCheckCartForm implements Serializable{
    private static final long serialVersionUID = 758087208773569549L;

    /**
     * skuid
     */
    @ApiModelProperty(required = false,value = "skuid",example = "1")
    @NotNull(message = "skuId不能为空")
    private Long skuId;

    /**
     * num
     */
    @ApiModelProperty(required = false,value = "数量")
    @NotBlank(message = "购买数量必填")
    private Integer num;

    /**
     * 是否选中
     */
    @ApiModelProperty(required = false,value = "是否选中",example = "0,1")
    @Min(message = "必须为数字且,1为开启,0为关闭", value = 0)
    @Max(message = "必须为数字且,1为开启,0为关闭", value = 1)
    private Integer checked;


}
