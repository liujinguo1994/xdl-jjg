package com.jjg.member.model.vo;

import com.shopx.goods.api.model.domain.cache.EsGoodsCO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author fk
 * @version v1.0
 * @Description: 商家查询商品使用
 * @date 2018/5/21 14:36
 * @since v7.0.0
 */
@ApiModel
@Data
public class LfcGoodsVO implements Serializable {

    private static final long serialVersionUID = 3922135264669953741L;
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(name="primaryPicUrl",value = "图片路径",required = true)
    private String primaryPicUrl;

    @ApiModelProperty(value = "商品名称")
    private String name;

    private String simpleDesc;

    private List<LfcSkuVO> skuList;

    private LfcItemDetailVO itemDetail;

    public LfcGoodsVO(EsGoodsCO goodsCO) {
        this.id = goodsCO.getId();
        this.primaryPicUrl = goodsCO.getOriginal();
        this.name = goodsCO.getGoodsName();
        this.simpleDesc = goodsCO.getMetaDescription();
    }




}


