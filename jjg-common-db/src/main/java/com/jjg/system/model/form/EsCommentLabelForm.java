package com.jjg.system.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

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
public class EsCommentLabelForm implements Serializable {


    private static final long serialVersionUID = -3432120379310598285L;

    /**
     * 评论标签内容
     */
    @ApiModelProperty(required = true, value = "评论标签内容")
    @NotBlank(message = "评论标签内容不能为空")
    private String commentLabel;

    /**
     * 标签类型( 0:商品，1物流，2服务)
     */
    @ApiModelProperty(required = true, value = "标签类型( 0:商品，1物流，2服务)", example = "1")
    @NotNull(message = "标签类型不能为空")
    private Integer type;

}
