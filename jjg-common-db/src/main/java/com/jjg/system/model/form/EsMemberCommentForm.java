package com.jjg.system.model.form;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 评论
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-04
 */
@Data
@ApiModel
public class EsMemberCommentForm extends QueryPageForm {


    private static final long serialVersionUID = -3617520511618775488L;
    /**
     * 关键字
     */
    @ApiModelProperty(value = "关键字")
    private String keyword;

}
