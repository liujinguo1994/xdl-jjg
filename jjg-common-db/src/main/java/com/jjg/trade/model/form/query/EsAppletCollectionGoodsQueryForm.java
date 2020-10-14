package com.xdl.jjg.model.form.query;

import com.shopx.common.model.result.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 小程序-商品收藏查询
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-07-14
 */
@Data
@ApiModel
public class EsAppletCollectionGoodsQueryForm extends QueryPageForm {

    /**
     * 查询类型
     */
    @ApiModelProperty(value = "查询类型1降价，2失效")
    private Integer type;
    /**
     * 分类id
     */
    @ApiModelProperty(value = "分类id")
    private Long categoryId;

    @ApiModelProperty(required = true,value = "登录态标识")
    @NotBlank(message = "登录态标识不能为空")
    private String skey;
}
