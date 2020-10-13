package com.xdl.jjg.model.form;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 配送方式Form
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-06-04
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsDeliveryModeForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 配送方式ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "配送方式ID")
    private Long deliveryModeId;

    /**
     * 配送方式名称(自提，配送)
     */
    @ApiModelProperty(value = "配送方式名称(自提，配送)")
    private String deliveryModeName;

}
