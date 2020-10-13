package com.xdl.jjg.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * <p>
 * 导入快递单号返回参数
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-03-27
 */
@Data
@ApiModel
public class EsImportShipVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "总导入个数")
    private Integer totalNum;

    @ApiModelProperty(value = "导入成功个数")
    private Integer successNum;

    @ApiModelProperty(value = "导入失败个数")
    private Integer failNum;

    @ApiModelProperty(value = "导入失败发货详情")
    private List<EsFailDataShipVO> failDataShip;
}