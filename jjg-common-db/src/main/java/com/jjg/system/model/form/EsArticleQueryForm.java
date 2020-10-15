package com.jjg.member.model.form;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 文章
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-07-24
 */
@Data
@ApiModel
public class EsArticleQueryForm extends QueryPageForm {


    private static final long serialVersionUID = 183028796064474389L;

    /**
     * 文章名称
     */
    @ApiModelProperty(value = "文章名称")
    private String articleName;

    /**
     * 分类id
     */
    @ApiModelProperty(value = "分类id", example = "1")
    private Long categoryId;

}
