package com.jjg.trade.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 小程序-删除商品收藏
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-07-14
 */
@Data
@ApiModel
public class EsAppletDelCollectionForm implements Serializable {

    private static final long serialVersionUID = -785021414818941920L;

    @ApiModelProperty(required = true,value = "登录态标识")
    @NotBlank(message = "登录态标识不能为空")
    private String skey;

    @ApiModelProperty(value = "商品ID集合")
    private List<Long> goodIds;
}
