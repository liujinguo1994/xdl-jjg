package com.xdl.jjg.model.vo;

import com.shopx.goods.api.model.domain.cache.EsGoodsSkuCO;
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
public class LfcSkuVO implements Serializable {

    private static final long serialVersionUID = 3922135264669953741L;
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(name = "pic_url", value = "sku图片")
    private String picUrl;

    @ApiModelProperty(name = "display_string",value = "sku规格文字信息")
    private String displayString;

    @ApiModelProperty(name = "item_sku_spec_value_list", value = "sku下的规格及规格值列表")
    private List<ItemSkuSpecValueListVO> itemSkuSpecValueList;

    public LfcSkuVO(EsGoodsSkuCO sku) {
        this.id = sku.getId();
        this.picUrl = sku.getThumbnail();
    }



}


