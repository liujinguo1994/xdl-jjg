package com.jjg.trade.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jjg.trade.model.domain.EsSelfTimeDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 自提日期
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
@ToString
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsSelfDateVO implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@ApiModelProperty(value = "主键ID")
	private Long id;
    /**
     * 自提日期
     */
	@ApiModelProperty(value = "自提日期")
	private Long selfDate;
    /**
     * 自提点ID
     */
	@ApiModelProperty(value = "自提点ID")
	private Long deliveryId;
	/**
	 * 有效状态
	 */
	@ApiModelProperty(value = "有效状态")
	private Integer state;

	/**
	 * 限制总人数
	 */
	@ApiModelProperty(value = "限制总人数")
	private Integer personTotal;

	/**
	 * 时间点list
	 */
    private List<EsSelfTimeDO> selfTimeDOList;


	protected Serializable pkVal() {
		return this.id;
	}

}
