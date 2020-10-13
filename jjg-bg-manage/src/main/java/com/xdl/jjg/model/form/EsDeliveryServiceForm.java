package com.xdl.jjg.model.form;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * <p>
 * 自提点信息维护Form
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-04
 */
@Data
@ApiModel
public class EsDeliveryServiceForm extends QueryPageForm {

    private static final long serialVersionUID = 8674284037224558832L;

    /**
     * 自提点名称
     */
    @ApiModelProperty(required = true, value = "自提点名称")
    @NotBlank(message = "自提点名称不能为空")
    private String deliveryName;

    /**
     * 门店地址
     */
    @ApiModelProperty(required = true, value = "门店地址")
    @NotBlank(message = "门店地址不能为空")
    private String address;

    /**
     * 有效状态（0：有效，1：无效）
     */
    @ApiModelProperty(required = true, value = "有效状态（0：有效，1：无效）", example = "1")
    @NotNull(message = "有效状态不能为空")
    private Integer state;

    /**
     * 自提日期id集合
     */
    @ApiModelProperty(required = true, value = "自提日期id集合")
    @Valid
    @NotNull(message = "自提日期id集合不能为空")
    @Size(min = 1, message = "至少有一个自提日期id")
    private List<Long> selfDateId;

    /**
     * 签约公司id
     */
    @ApiModelProperty(required = true, value = "签约公司id", example = "1")
    @NotNull(message = "签约公司id不能为空")
    private Long companyId;
}
