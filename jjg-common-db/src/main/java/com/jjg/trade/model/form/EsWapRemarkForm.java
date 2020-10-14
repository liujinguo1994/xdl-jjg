package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 移动端-订单备注
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2020-03-03 13:28:26
 */
@Data
@ApiModel
public class EsWapRemarkForm implements Serializable {


    private static final long serialVersionUID = 3863114069985817704L;
    /**
     * 店铺id
     */
    @ApiModelProperty(required = false,value = "店铺id")
    @NotNull(message = "店铺id不能为空")
    private Long shopId;


    /**
     * 备注
     */
    @ApiModelProperty(required = false,value = "备注")
    private String remark;

}
