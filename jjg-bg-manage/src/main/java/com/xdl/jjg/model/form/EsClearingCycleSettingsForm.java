package com.xdl.jjg.model.form;

import com.shopx.system.api.model.domain.vo.EsClearingCycleVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 结算周期设置FORM
 */
@Data
@ApiModel
public class EsClearingCycleSettingsForm implements Serializable {

    private static final long serialVersionUID = -1549672177047538309L;

    /**
     * 结算周期设置
     */
    @ApiModelProperty(value = "结算周期设置")
    private List<EsClearingCycleVO> ClearingCycleSettings;

}
