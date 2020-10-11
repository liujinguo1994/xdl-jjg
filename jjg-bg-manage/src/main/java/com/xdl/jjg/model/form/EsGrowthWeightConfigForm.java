package com.xdl.jjg.model.form;

import com.shopx.member.api.model.domain.vo.EsGrowthWeightConfigVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 成长值权重设置
 * </p>
 *
 */
@Data
@ApiModel
public class EsGrowthWeightConfigForm implements Serializable {

    private static final long serialVersionUID = 6574394843208867912L;
    /**
     * 成长值权重设置集合
     */
    @ApiModelProperty(value = "成长值权重设置集合")
    List<EsGrowthWeightConfigVO> weightVoS;
}
