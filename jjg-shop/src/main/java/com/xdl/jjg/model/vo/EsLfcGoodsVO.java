package com.jjg.member.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 人寿商品
 * @author yuanj
 * @version v2.0
 * @since v7.0.0
 * 2020-03-25 16:32:45
 */
@Data
@ApiModel
public class EsLfcGoodsVO implements Serializable {
			
    private static final long serialVersionUID = 9122931201151887L;

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    private Long id;
    /**商品id*/
    @ApiModelProperty(value = "商品id")
    private Long goodsId;

    @ApiModelProperty(value = "审核状态 1审核通过 2 待审核")
    private Integer isAuth;

}