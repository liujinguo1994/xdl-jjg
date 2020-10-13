package com.xdl.jjg.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 会员活跃度配置表
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-07-02 19:50:03
 */
@Data
@ApiModel
public class EsSellerMemberActiveConfigVO implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 天数
     */
    @ApiModelProperty(required = false,value = "天数",example = "1")
    private Integer days;

    /**
     * 会员类型 0新增，1活跃，2休眠，3普通
     */
    @ApiModelProperty(required = false,value = "会员类型 0新增，1活跃，2休眠",example = "1")
    private Integer memberType;
    /**
     * 订单数
     */
    @ApiModelProperty(required = false,value = "订单数",example = "1")
    private Integer orders;
    /**
     * 未访问天数
     */
    @ApiModelProperty(required = false,value = "未访问天数",example = "1")
    private Integer visitDays;

}
