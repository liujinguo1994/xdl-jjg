package com.jjg.system.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 签约公司
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel
public class EsCompanyForm implements Serializable {


    private static final long serialVersionUID = 4218143836452851876L;

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID", example = "1")
    private Long id;
    /**
     * 公司名称
     */
    @ApiModelProperty(required = true, value = "公司名称")
    @NotBlank(message = "公司名称不能为空")
    private String companyName;
    /**
     * 公司编号
     */
    @ApiModelProperty(required = true, value = "公司编号")
    @NotBlank(message = "公司编号不能为空")
    private String companyCode;
    /**
     * 创建时间
     */
    @ApiModelProperty(required = true, value = "创建时间", example = "1")
    @NotNull(message = "创建时间不能为空")
    private Long createTime;
    /**
     * 结束时间
     */
    @ApiModelProperty(required = true, value = "结束时间", example = "1")
    @NotNull(message = "结束时间不能为空")
    private Long endTime;
    /**
     * 是否有效0为正常，1为禁用
     */
    @ApiModelProperty(required = true, value = "是否有效(0:有效，1:无效)", example = "1")
    @NotNull(message = "状态不能为空")
    private Integer state;


}
