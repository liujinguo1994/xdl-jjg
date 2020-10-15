package com.jjg.trade.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 商品查询条件
 *
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018年3月21日 下午3:46:04
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
public class EsCakeCardQueryDTO implements Serializable {
    /**
     * 页码
     */
    @ApiModelProperty(name = "page_no", value = "页码", required = false)
    private Integer pageNum;
    /**
     * 分页数
     */
    @ApiModelProperty(name = "page_size", value = "分页数", required = false)
    private Integer pageSize;
    /**
     * 是否上架 0代表已下架，1代表已上架
     */
    @ApiModelProperty(name = "is_used", value = "是否可用，0正常，1用过")
    private Integer isUsed;
    /**
     * 卡券码
     */
    @ApiModelProperty(name = "code", value = "卡券码")
    private String code;

    /**
     * 订单号
     */
    @ApiModelProperty(name = "order_sn", value = "卡券码")
    private String orderSn;

}
