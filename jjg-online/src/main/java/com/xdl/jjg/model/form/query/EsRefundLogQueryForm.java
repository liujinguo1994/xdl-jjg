package com.xdl.jjg.model.form.query;

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
public class EsRefundLogQueryForm implements Serializable {

    private static final long serialVersionUID = 1L;

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

}
