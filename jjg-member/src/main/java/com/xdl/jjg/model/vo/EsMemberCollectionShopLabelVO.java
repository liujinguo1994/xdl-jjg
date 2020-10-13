package com.xdl.jjg.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 会员商品收藏
 * </p>
 *
 * @author HQL 236154186@qq.com
 * @since 2019-05-29
 */
@Data
@Api
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsMemberCollectionShopLabelVO implements Serializable {
    /**
     * 标签ID
     */
    @ApiModelProperty(required = false,value = "标签ID")
    private Long id;

    /**
     * 标签名字
     */
    @ApiModelProperty(required = false,value = "标签名称")
    private String tagName;
    /**
     * 商品集合
     */
    @ApiModelProperty(required = false,value = "商品集合")
    private List<EsMemberGoodsVO> memberGoodsVO;
}
