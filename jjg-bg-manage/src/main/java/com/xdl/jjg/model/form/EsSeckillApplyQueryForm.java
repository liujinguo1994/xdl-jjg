package com.xdl.jjg.model.form;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 限时抢购商品
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel
public class EsSeckillApplyQueryForm extends QueryPageForm {


    private static final long serialVersionUID = 4785665812171456168L;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    /**
     * 活动id
     */
    @ApiModelProperty(required = true, value = "活动id", example = "1")
    @NotNull(message = "活动id不能为空")
    private Long seckillId;
    /**
     * 申请状态(0:待审核，1:通过审核,2:未通过)
     */
    @ApiModelProperty(required = true, value = "申请状态(0:待审核，1:通过审核,2:未通过)", example = "1")
    @NotNull(message = "申请状态不能为空")
    private Integer state;

}
