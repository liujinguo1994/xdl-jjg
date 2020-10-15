package com.jjg.trade.model.vo;

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
public class EsRefundLogVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@ApiModelProperty(value = "主键ID")
	private Long id;
    /**
     * 退款sn
     */
	@ApiModelProperty(value = "退款sn")
	private String refundSn;
    /**
     * 日志记录时间
     */
	@ApiModelProperty(value = "日志记录时间")
	private Long createTime;
    /**
     * 日志详细
     */
	@ApiModelProperty(value = "日志详细")
	private String logdetail;
    /**
     * 操作者
     */
	@ApiModelProperty(value = "操作者")
	private String operator;


	protected Serializable pkVal() {
		return this.id;
	}

}
