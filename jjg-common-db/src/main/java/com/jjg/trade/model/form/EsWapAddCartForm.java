package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 移动端-添加购物车
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2020-02-27 13:28:26
 */
@Data
@ApiModel
public class EsWapAddCartForm implements Serializable {


    private static final long serialVersionUID = 3863114069985817704L;
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
    @NotNull(message = "购买数量必填")
    private Integer num;

}
