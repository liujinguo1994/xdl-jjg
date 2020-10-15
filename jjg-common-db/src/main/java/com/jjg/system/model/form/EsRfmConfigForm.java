package com.jjg.system.model.form;

import com.shopx.member.api.model.domain.vo.EsMemberRfmConfigVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel
public class EsRfmConfigForm implements Serializable {

    private static final long serialVersionUID = 5308301793108867607L;

    /**
     * rfm配置集合
     */
    @ApiModelProperty(value = "rfm配置集合")
    private List<EsMemberRfmConfigVO> rfmConfigVOList;
}
