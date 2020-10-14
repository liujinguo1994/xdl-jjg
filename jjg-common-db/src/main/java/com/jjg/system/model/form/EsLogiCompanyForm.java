package com.xdl.jjg.model.form;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 物流公司
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel
public class EsLogiCompanyForm extends QueryPageForm {

    private static final long serialVersionUID = 5416578196384174858L;
    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID", example = "1")
    private Long id;
    /**
     * 物流公司名称
     */
    @ApiModelProperty(required = true, value = "物流公司名称")
    @NotBlank(message = "物流公司名称不能为空")
    private String name;
    /**
     * 物流公司code
     */
    @ApiModelProperty(required = true, value = "物流公司code")
    @NotBlank(message = "物流公司code不能为空")
    private String code;
    /**
     * 快递鸟物流公司code
     */
    @ApiModelProperty(required = true, value = "快递鸟物流公司code")
    @NotBlank(message = "快递鸟物流公司code不能为空")
    private String kdcode;
    /**
     * 是否支持电子面单1：支持 0：不支持
     */
    @ApiModelProperty(required = true, value = "是否支持电子面单（1：支持， 0：不支持）", example = "1")
    @NotNull(message = "是否支持电子面单不能为空")
    private Integer isWaybill;
    /**
     * 是否有效 0 有效 1 无效
     */
    @ApiModelProperty(required = true, value = "是否有效（ 0 有效 ，1 无效）", example = "1")
    @NotNull(message = "是否有效不能为空")
    private Integer state;

    /**
     * 物流公司客户号
     */
    @ApiModelProperty(value = "物流公司客户号")
    private String customerName;

    /**
     * 物流公司电子面单密码
     */
    @ApiModelProperty(value = "物流公司电子面单密码")
    private String customerPwd;

    @ApiModelProperty("快递公司官方电话")
    private String phone;

}
