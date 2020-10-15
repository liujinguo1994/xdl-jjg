package com.jjg.trade.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 品牌实体
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018-03-16 16:32:45
 */
@ApiModel
@Data
public class EsCakeCardVO implements Serializable {
			
    private static final long serialVersionUID = 9122931201151887L;
    
    /**卡券主键*/

    @ApiModelProperty(hidden=true)
    private Integer id;
    /**卡券名称*/

    @ApiModelProperty(value="卡券名称",required=true)
    private String name;
    /**卡券码*/
    @ApiModelProperty(value="卡券码",required=true)
    private String code;
    /**卡券码密码*/

    @ApiModelProperty(value="卡券码密码",required=true)
    private String password;


    /**下单人手机号*/

    @ApiModelProperty(name="mobile",value="下单人手机号",required=false)
    private String mobile;


    /**订单号*/

    @ApiModelProperty(name="order_sn",value="订单号",required=false)
    private String orderSn;
    /**是否可用，0正常，1用过*/

    @ApiModelProperty(hidden=true)
    private Integer isUsed;

    /**创建时间*/

    @ApiModelProperty(name="create_time",value="创建时间",required=false)
    private Long createTime;

    /**更新时间*/

    @ApiModelProperty(name="update_time",value="更新时间",required=false)
    private Long updateTime;

}