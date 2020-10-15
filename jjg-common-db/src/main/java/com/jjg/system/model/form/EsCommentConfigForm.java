package com.jjg.system.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 商品好评设置form
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-29 14:18:12
 */
@Data
@ApiModel
public class EsCommentConfigForm implements Serializable {


    private static final long serialVersionUID = -4939521063856293052L;


    /**
     * 权重设置集合
     */
    @ApiModelProperty(required = true, value = "权重设置集合")
    @Valid
    @NotNull(message = "权重设置集合不能为空")
    @Size(min = 1, message = "至少有一个权重信息")
    private List<EsGradeWeightConfigForm> gradeWeightConfigFormList;

    /**
     * 好中差评设置集合
     */
    @ApiModelProperty(required = true, value = "好中差评设置集合")
    @Valid
    @NotNull(message = "好中差评设置集合不能为空")
    @Size(min = 1, message = "至少有一个好中差评设置")
    private List<EsCommentSortConfigForm> commentSortConfigFormList;


}
