package com.jjg.system.model.form;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 商品评论标签
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-04
 */
@Data
@ApiModel
public class EsCommentLabelQueryForm extends QueryPageForm {


    private static final long serialVersionUID = -3432120379310598285L;

    /**
     * 输入框输入值（评论标签内容）
     */
    @ApiModelProperty(value = "输入框输入值（评论标签内容）")
    private String keyword;

    /**
     * 标签类型( 0:商品，1物流，2服务)
     */
    @ApiModelProperty(value = "标签类型( 0:商品，1物流，2服务)", example = "1")
    private Integer type;

}
