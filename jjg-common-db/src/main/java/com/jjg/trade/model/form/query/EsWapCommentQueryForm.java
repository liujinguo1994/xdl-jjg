package com.jjg.trade.model.form.query;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;


/**
 * <p>
 * 移动端-评论查询form
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-02-17 09:28:26
 */
@Data
@ApiModel
public class EsWapCommentQueryForm extends QueryPageForm {


    private static final long serialVersionUID = 3863114069985817704L;
    /**
     * 类型 2待评价，1已评价
     */
    @ApiModelProperty(value = "类型 2待评价，1已评价")
    @NotNull(message = "类型不能为空")
    private Integer type;
    @ApiModelProperty(value = "订单编号")
    private String orderSn;

}
