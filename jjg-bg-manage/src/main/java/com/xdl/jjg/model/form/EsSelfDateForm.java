package com.xdl.jjg.model.form;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * <p>
 * 自提日期
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel
public class EsSelfDateForm extends QueryPageForm {


    private static final long serialVersionUID = -6563702032275869581L;

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID", example = "1")
    private Long id;
    /**
     * 自提日期
     */
    @ApiModelProperty(required = true, value = "自提日期", example = "1")
    @NotNull(message = "自提日期不能为空")
    private Long selfDate;
    /**
     * 有效状态（0：有效，1：无效）
     */
    @ApiModelProperty(required = true, value = "有效状态（0：有效，1：无效）", example = "1")
    @NotNull(message = "有效状态不能为空")
    private Integer state;
    /**
     * 自提时间段集合
     */
    @ApiModelProperty(required = true, value = "自提时间段集合")
    @Valid
    @NotNull(message = "自提时间段集合不能为空")
    @Size(min = 1, message = "至少有一个自提时间段")
    private List<EsSelfTimeForm> selfTimeFormList;

}
