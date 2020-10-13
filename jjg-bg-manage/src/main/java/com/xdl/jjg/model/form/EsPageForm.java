package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * <p>
 * 楼层
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-04
 */
@Data
@ApiModel
public class EsPageForm implements Serializable {


    private static final long serialVersionUID = 5300159199677031568L;
    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id", example = "1")
    private Long id;
    /**
     * 楼层名称
     */
    @ApiModelProperty(value = "楼层名称", required = true)
    @NotBlank(message = "名称不能为空")
    private String pageName;
    /**
     * 楼层数据
     */
    @ApiModelProperty(value = "楼层数据", required = true)
    @NotBlank(message = "页面数据不能为空")
    private String pageData;
    /**
     * 页面类型
     */
    @ApiModelProperty(value = "页面类型", required = true)
    @NotBlank(message = "页面类型不能为空")
    private String pageType;
    /**
     * 客户端类型
     */
    @ApiModelProperty(value = "客户端类型", required = true)
    @NotBlank(message = "客户端类型不能为空")
    private String clientType;

}
