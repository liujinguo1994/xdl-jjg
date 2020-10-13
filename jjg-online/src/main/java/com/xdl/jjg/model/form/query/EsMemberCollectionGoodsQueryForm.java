package com.xdl.jjg.model.form.query;

import com.shopx.common.model.result.QueryPageForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 查询会员商品收藏
 * </p>
 * @author xl 236154186@qq.com
 * @since 2019-12-16
 */
@Data
@Api
public class EsMemberCollectionGoodsQueryForm extends QueryPageForm {


    /**
     * 查询类型
     */
    @ApiModelProperty(required = false, value = "查询类型1降价，2失效", example = "")
    private Integer type;
    /**
     * 分类id
     */
    @ApiModelProperty(required = false,value = "分类id",example = "")
    private Long categoryId;
}
