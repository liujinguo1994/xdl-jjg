package com.xdl.jjg.model.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 会员实体
 *
 * @author wangaf
 * @version v7.0.0
 * @since v7.0.0
 */
@ApiModel
@Data
public class EsMemberSellerVO implements Serializable {

    /**
     * 会员ID
     */
    @ApiModelProperty(value = "会员ID")
    private Long id;
    /**
     * 会员登陆用户名
     */
    @ApiModelProperty(value = "会员登陆用户名")
    private String name;
    /**
     * 真实姓名
     */
    @ApiModelProperty(value = "真实姓名")
    private String nickname;
    /**
     * 会员头像
     */
    @ApiModelProperty(value = "会员头像")
    private String face;

    private Long userId;
    /**
     * token
     */
    private String token;

    public EsMemberSellerVO() {

    }

}