package com.jjg.member.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
@ToString
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsSeckillRangeVO implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@ApiModelProperty(value = "主键ID")
	private Long id;
    /**
     * 限时抢购活动id
     */
	@ApiModelProperty(value = "限时抢购活动id")
	private Long seckillId;
    /**
     * 整点时刻
     */
	@ApiModelProperty(value = "整点时刻")
	private Integer rangeTime;


	protected Serializable pkVal() {
		return this.id;
	}

}
