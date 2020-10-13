package com.xdl.jjg.model.form.query;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 自提日期QueryForm
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-06-05 09:25:43
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsSelfDateQueryForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自提日期
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "自提日期")
	private Long selfDate;

    /**
     * 自提点ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "自提点ID")
	private Long deliveryId;

}
