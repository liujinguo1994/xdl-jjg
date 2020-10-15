package com.jjg.system.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 会员等级配置
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-07-04 09:42:04
 */
@Data
@ApiModel
public class EsMemberLevelConfigForm implements Serializable {


    private static final long serialVersionUID = 6872818481551774931L;
    /**
     * 等级名称
     */
    @ApiModelProperty(value = "等级名称")
    private String level;

    /**
     * 成长值下线
     */
    @ApiModelProperty(value = "成长值下线", example = "1")
    private Integer underLine;
}
