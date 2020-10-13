package com.xdl.jjg.model.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * <p>
 * 导入失败余额详情
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-03-27
 */
@Data
@ApiModel
public class EsImportBalanceFailDataVO implements Serializable {

    private static final long serialVersionUID = -3589717115274123659L;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "修改余额")
    private String updateBal;

}