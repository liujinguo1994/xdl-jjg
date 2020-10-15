package com.jjg.trade.model.form.query;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 自提点信息维护QueryForm
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-06-04
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsDeliveryServiceQueryForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 门店地址
     */
    @ApiModelProperty(value = "门店地址")
    private String address;

    /**
     * 联系电话
     */
    @ApiModelProperty(value = "联系电话")
    private String phone;

    /**
     * 店铺ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "店铺ID")
    private Long shopId;

}
