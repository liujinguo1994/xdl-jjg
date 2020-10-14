package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 签约公司结算单Form
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-05 14:28:41
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsBillForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 结算状态 0 已结算 1 未结算
     */
	@ApiModelProperty(value = "结算状态 0 已结算 1 未结算")
	private Integer state;
}
