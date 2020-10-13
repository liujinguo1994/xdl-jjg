package com.xdl.jjg.model.form;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 商品Form
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-05 17:30:01
 */
@Data
@ApiModel
public class EsGoodsQueryForm extends QueryPageForm {
    private static final long serialVersionUID = 2591125693714921689L;
    /**
     * 输入框输入值（商品编号或者商品名称）
     */
    @ApiModelProperty(value = "输入框输入值")
    private String keyword;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    /**
     * 商品编号
     */
    @ApiModelProperty(value = "商品编号")
    private String goodsSn;
    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String shopName;
    /**
     * 供应商名称
     */
    @ApiModelProperty("供应商名称")
    private String supplierName;
    /**
     * 审核通过时间(上架时间开始)
     */
    @ApiModelProperty(value = "审核通过时间(上架时间开始)", example = "1")
    private Long authTimeStart;
    /**
     * 审核通过时间(上架时间结束)
     */
    @ApiModelProperty(value = "审核通过时间(上架时间结束)", example = "1")
    private Long authTimeEnd;
    /**
     * 分类路径
     */
    @ApiModelProperty(value = "分类路径")
    private String categoryPath;
}
