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
 * 自提时间QueryForm
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-06-05 09:25:43
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsSelfTimeQueryForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 开始时间
     */
	@ApiModelProperty(value = "开始时间")
	private Long createTime;

    /**
     * 结束时间
     */
	@ApiModelProperty(value = "结束时间")
	private Long updateTime;

    /**
     * 次数
     */
	@ApiModelProperty(value = "次数")
	private Integer number;

    /**
     * 自提点日期ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "自提点日期ID")
	private Long deliveryId;

}
