package com.xdl.jjg.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.shopx.goods.api.model.domain.EsGoodsSkuDO;
import com.shopx.goods.api.model.domain.EsSpecValuesDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 商品sku
 *
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018年3月21日 上午11:50:42
 */
@Data
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoodsSkuVO extends EsGoodsSkuDO {

    private static final long serialVersionUID = -666090547834195127L;

    @ApiModelProperty(name = "spec_list", value = "规格列表", required = false)
    private List<EsSpecValuesDO> specList;

    @ApiModelProperty(name = "goods_transfee_charge", value = "谁承担运费0：买家承担，1：卖家承担", hidden = true)
    private Integer goodsTransfeeCharge;

    @ApiModelProperty(value = "是否被删除 0 删除 1 未删除", hidden = true)
    private Integer disabled;

    @ApiModelProperty(value = "上架状态  0下架 1上架", hidden = true)
    private Integer marketEnable;

}
