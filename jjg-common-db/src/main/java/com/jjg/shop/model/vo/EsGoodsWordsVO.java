package com.jjg.shop.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 自定义分词
 * </p>
 *
 * @author WANGAF 826988665@qq.com
 * @since 2019-07-01 13:54:20
 */
@Data
@ApiModel
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown=true)
public class EsGoodsWordsVO implements  Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分词名称
     */
    @ApiModelProperty(value = "分词名称")
	private String words;

    /**
     * 商品数量
     */
    @ApiModelProperty(value = "商品数量")
	private Long goodsNum;

    /**
     * 全拼
     */
    @ApiModelProperty(value = "全拼")
	private String quanpin;

    /**
     * 首字母
     */
    @ApiModelProperty(value = "首字母")
	private String szm;

}
