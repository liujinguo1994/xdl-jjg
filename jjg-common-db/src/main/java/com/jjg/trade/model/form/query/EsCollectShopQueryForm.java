package com.jjg.trade.model.form.query;


import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 查询收藏店铺列表
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel
public class EsCollectShopQueryForm extends QueryPageForm {
    private static final long serialVersionUID = 1L;
    /**
     * 店铺名称
     */
    @ApiModelProperty(required = false,value = "店铺名称")
    private String shopName;
    /**
     * 标签tagId
     */
    @ApiModelProperty(required = false,value = "1:热销 2:新品")
    private Long tagId;

}
