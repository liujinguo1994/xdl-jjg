package com.xdl.jjg.model.form;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 会员余额明细
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-04
 */
@Data
@ApiModel
public class EsMemberDepositQueryForm extends QueryPageForm {


    private static final long serialVersionUID = 7879643292754468799L;


    /**
     * 会员id
     */
    @ApiModelProperty(required = true,value = "会员id",example = "1")
    @NotNull(message = "会员id不能为空")
	private Long memberId;

    /**
     * 操作类型(1充值，2消费，3退款)
     */
    @ApiModelProperty(value = "操作类型(1充值，2消费，3退款)")
	private String type;
    /**
     * 交易时间开始
     */
    @ApiModelProperty(value = "交易时间开始",example = "1")
	private Long createTimeStart;
    /**
     * 交易时间结束
     */
    @ApiModelProperty(value = "交易时间结束",example = "1")
	private Long createTimeEnd;


}
