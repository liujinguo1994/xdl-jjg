package com.jjg.system.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 导航菜单
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel
public class EsSiteNavigationForm implements Serializable {

    private static final long serialVersionUID = 349771739453781318L;
    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID", example = "1")
    private Long id;
    /**
     * 导航名称
     */
    @ApiModelProperty(required = true, value = "导航名称")
    @NotBlank(message = "导航名称不能为空")
    private String navigationName;
    /**
     * 导航地址
     */
    @ApiModelProperty(required = true, value = "导航地址")
    @NotBlank(message = "导航地址不能为空")
    private String url;
    /**
     * 客户端类型
     */
    @ApiModelProperty(required = true, value = "客户端类型")
    @NotBlank(message = "客户端类型不能为空")
    private String clientType;
    /**
     * 图片
     */
    @ApiModelProperty(value = "图片")
    private String image;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    @NotNull(message = "排序不能为空")
    private Integer sort;
}
