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
 * QueryForm
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-06-05 09:25:43
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsSeckillRangeQueryForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 限时抢购活动id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "限时抢购活动id")
	private Long seckillId;

    /**
     * 整点时刻
     */
	@ApiModelProperty(value = "整点时刻")
	private Integer rangeTime;

}
